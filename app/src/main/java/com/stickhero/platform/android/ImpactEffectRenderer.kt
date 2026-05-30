package com.stickhero.platform.android

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import com.stickhero.game.renderstate.ImpactEffectRenderModel
import java.io.IOException
import kotlin.math.abs

class ImpactEffectRenderer(
    context: Context,
    private val paint: Paint
) {
    private val impact = loadImpactSheet(context)

    fun draw(canvas: Canvas, effects: List<ImpactEffectRenderModel>) {
        val sheet = impact ?: return
        effects.forEach { effect ->
            val frame = sheet.frameFor(effect.progress)
            val alpha = ((1f - effect.progress) * 255f).toInt().coerceIn(0, 255)
            val scale = (effect.strength * 0.45f) + effect.progress * 0.08f
            val width = frame.source.width().toFloat() * scale
            val height = frame.source.height().toFloat() * scale
            val dest = RectF(
                effect.position.x - width * 0.5f,
                effect.position.y - height * 0.5f,
                effect.position.x + width * 0.5f,
                effect.position.y + height * 0.5f
            )
            paint.alpha = alpha
            canvas.drawBitmap(sheet.bitmap, frame.source, dest, paint)
        }
        paint.alpha = 255
    }

    private fun loadImpactSheet(context: Context): SpriteImpactSheet? {
        val path = "spritesheets/VFXKameImpact.png"
        val decoded = try {
            context.assets.open(path).use { input ->
                BitmapFactory.decodeStream(input, null, BitmapFactory.Options().apply {
                    inPreferredConfig = Bitmap.Config.ARGB_8888
                })
            }
        } catch (_: IOException) {
            null
        } ?: return null

        val transparent = makeTransparent(decoded)
        val frames = buildFrames(transparent, 3, 3)
        return SpriteImpactSheet(transparent, frames, 0.28f)
    }

    private fun makeTransparent(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val key = averageCornerColor(pixels, width, height)
        val threshold = if (brightness(key) < 32) 18 else 24
        val queue = IntArray(width * height)
        var head = 0
        var tail = 0

        fun push(index: Int) {
            val color = pixels[index]
            if ((color ushr 24) == 0) return
            if (colorDistance(color, key) > threshold) return
            pixels[index] = color and 0x00FFFFFF
            queue[tail++] = index
        }

        push(0)
        push(width - 1)
        push((height - 1) * width)
        push(height * width - 1)

        while (head < tail) {
            val index = queue[head++]
            val x = index % width
            val y = index / width
            if (x > 0) push(index - 1)
            if (x + 1 < width) push(index + 1)
            if (y > 0) push(index - width)
            if (y + 1 < height) push(index + width)
        }

        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setHasAlpha(true)
            setPixels(pixels, 0, width, 0, 0, width, height)
        }
    }

    private fun buildFrames(bitmap: Bitmap, columns: Int, rows: Int): List<Rect> {
        val cellWidth = bitmap.width / columns
        val cellHeight = bitmap.height / rows
        val frames = ArrayList<Rect>(columns * rows)
        for (row in 0 until rows) {
            for (col in 0 until columns) {
                val left = col * cellWidth
                val top = row * cellHeight
                val right = if (col == columns - 1) bitmap.width else (col + 1) * cellWidth
                val bottom = if (row == rows - 1) bitmap.height else (row + 1) * cellHeight
                frames += tightBounds(bitmap, Rect(left, top, right, bottom))
            }
        }
        return frames
    }

    private fun tightBounds(bitmap: Bitmap, frame: Rect): Rect {
        val row = IntArray(frame.width())
        var left = frame.width()
        var top = frame.height()
        var right = -1
        var bottom = -1
        for (y in frame.top until frame.bottom) {
            bitmap.getPixels(row, 0, frame.width(), frame.left, y, frame.width(), 1)
            for (x in row.indices) {
                if ((row[x] ushr 24) != 0) {
                    val localY = y - frame.top
                    if (x < left) left = x
                    if (localY < top) top = localY
                    if (x > right) right = x
                    if (localY > bottom) bottom = localY
                }
            }
        }
        if (right < left || bottom < top) return frame
        val padding = 2
        return Rect(
            (frame.left + left - padding).coerceAtLeast(frame.left),
            (frame.top + top - padding).coerceAtLeast(frame.top),
            (frame.left + right + padding + 1).coerceAtMost(frame.right),
            (frame.top + bottom + padding + 1).coerceAtMost(frame.bottom)
        )
    }

    private fun averageCornerColor(pixels: IntArray, width: Int, height: Int): Int {
        val corners = intArrayOf(0, width - 1, (height - 1) * width, height * width - 1)
        var r = 0
        var g = 0
        var b = 0
        corners.forEach { index ->
            val color = pixels[index]
            r += (color shr 16) and 0xFF
            g += (color shr 8) and 0xFF
            b += color and 0xFF
        }
        return (0xFF shl 24) or ((r / 4) shl 16) or ((g / 4) shl 8) or (b / 4)
    }

    private fun brightness(color: Int): Int {
        return (((color shr 16) and 0xFF) + ((color shr 8) and 0xFF) + (color and 0xFF)) / 3
    }

    private fun colorDistance(a: Int, b: Int): Int {
        val dr = abs(((a shr 16) and 0xFF) - ((b shr 16) and 0xFF))
        val dg = abs(((a shr 8) and 0xFF) - ((b shr 8) and 0xFF))
        val db = abs((a and 0xFF) - (b and 0xFF))
        return dr + dg + db
    }
}

private data class SpriteImpactSheet(
    val bitmap: Bitmap,
    val frames: List<Rect>,
    val durationSeconds: Float
) {
    fun frameFor(progress: Float): VfxFrame {
        if (frames.size == 1) return VfxFrame(frames.first())
        val index = ((progress.coerceIn(0f, 1f)) * frames.lastIndex).toInt().coerceIn(0, frames.lastIndex)
        return VfxFrame(frames[index])
    }
}

private data class VfxFrame(
    val source: Rect
)
