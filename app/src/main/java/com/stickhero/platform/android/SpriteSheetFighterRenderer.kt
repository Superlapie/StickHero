package com.stickhero.platform.android

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import com.stickhero.game.animation.PoseLibrary
import com.stickhero.game.animation.StickAnimationLibrary
import com.stickhero.game.config.AttackCatalog
import com.stickhero.game.fighter.FacingDirection
import com.stickhero.game.fighter.FighterState
import com.stickhero.game.physics.Vec2
import com.stickhero.game.renderstate.FighterRenderModel
import java.io.IOException
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

private const val JumpStartSeconds = 0.18f
private const val LandStartSeconds = 0.55f
private const val KameChargeSeconds = 0.42f
private const val KameOriginForward = 16f
private const val KameOriginDrop = 8f
private const val FighterRenderHeight = 153f
private const val FighterRenderWidthRatio = 0.90f
private const val EarthSmashEndDrop = 8f

class SpriteSheetFighterRenderer(
    context: Context,
    private val paint: Paint
) {
    private val assets = SpriteAssetLibrary(context)

    fun draw(canvas: Canvas, fighter: FighterRenderModel, worldHeight: Float) {
        drawFighter(canvas, fighter, worldHeight)
        if (fighter.pose.clipId == PoseLibrary.HEAVY_PUNCH) {
            drawKamehameha(canvas, fighter, worldHeight)
        }
    }

    private fun drawFighter(canvas: Canvas, fighter: FighterRenderModel, worldHeight: Float) {
        val selection = when (fighter.pose.clipId) {
            PoseLibrary.JAB, PoseLibrary.CROSS -> SheetSelection("Jab", fighter.animationTime)
            PoseLibrary.HURT_LIGHT, PoseLibrary.HURT_HEAVY -> SheetSelection("Hurt", fighter.animationTime)
            PoseLibrary.KNOCKOUT -> SheetSelection("KnockedOut", fighter.animationTime)
            PoseLibrary.CROUCH -> selectCrouchSheet(fighter)
            PoseLibrary.JUMP -> selectJumpSheet(fighter)
            PoseLibrary.WALK_FORWARD, PoseLibrary.WALK_BACKWARD -> SheetSelection("Walk", fighter.animationTime)
            PoseLibrary.HEAVY_PUNCH -> selectKameSheet(fighter)
            PoseLibrary.EARTH_SMASH -> SheetSelection("EarthSmash", fighter.animationTime)
            PoseLibrary.BLADE_FLURRY -> SheetSelection("BladeFlurry", fighter.animationTime)
            PoseLibrary.ELECTRO_PULSE -> SheetSelection("ElectroPulse", fighter.animationTime)
            PoseLibrary.FLAME_BURST -> SheetSelection("FlameBurst", fighter.animationTime)
            else -> SheetSelection("Idlespritesheet", fighter.animationTime)
        }

        drawSheet(canvas, fighter, worldHeight, assets.sheet(selection.name) ?: return, selection.timeSeconds)
    }

    private fun selectCrouchSheet(fighter: FighterRenderModel): SheetSelection {
        return when {
            fighter.crouchAmount > 0.72f -> SheetSelection("CrouchIdle", fighter.animationTime)
            fighter.crouchAmount > 0.30f -> SheetSelection("CrouchDown", fighter.animationTime)
            else -> SheetSelection("CrouchUp", fighter.animationTime)
        }
    }

    private fun selectJumpSheet(fighter: FighterRenderModel): SheetSelection {
        return when {
            fighter.velocityY > 12f -> SheetSelection("Fall", fighter.animationTime)
            fighter.velocityY < -12f -> {
                if (fighter.animationTime < JumpStartSeconds) {
                    SheetSelection("JumpStart", fighter.animationTime)
                } else {
                    SheetSelection("JumpRise", fighter.animationTime - JumpStartSeconds)
                }
            }
            fighter.animationTime < JumpStartSeconds -> SheetSelection("JumpStart", fighter.animationTime)
            fighter.animationTime > LandStartSeconds -> SheetSelection("Land", fighter.animationTime - LandStartSeconds)
            else -> SheetSelection("JumpRise", fighter.animationTime - JumpStartSeconds)
        }
    }

    private fun selectKameSheet(fighter: FighterRenderModel): SheetSelection {
        return when (fighter.pose.attackPhase) {
            "startup" -> {
                if (fighter.pose.attackElapsedSeconds < KameChargeSeconds) {
                    SheetSelection("KameCharge", fighter.pose.attackVisualElapsedSeconds)
                } else {
                    SheetSelection("KameChargeRelease", fighter.pose.attackVisualElapsedSeconds - KameChargeSeconds)
                }
            }
            "active" -> SheetSelection("KameFire", fighter.pose.attackVisualElapsedSeconds - AttackCatalog.kamehameha.startupDuration)
            "recovery" -> SheetSelection(
                "KameRelease",
                fighter.pose.attackElapsedSeconds - AttackCatalog.kamehameha.startupDuration - AttackCatalog.kamehameha.activeDuration
            )
            else -> SheetSelection("KameCharge", fighter.animationTime)
        }
    }

    private fun drawSheet(
        canvas: Canvas,
        fighter: FighterRenderModel,
        worldHeight: Float,
        sheet: SpriteSheetAsset,
        timeSeconds: Float
    ) {
        val frame = sheet.frameFor(timeSeconds)
        val scale = (worldHeight / 720f) * sheet.renderHeight / sheet.maxContentHeight.coerceAtLeast(1f)
        val source = frame.source
        val content = frame.contentBounds
        val destLeft = fighter.x - (content.exactCenterX() - source.left) * scale
        val destTop = fighter.groundY - (content.bottom - source.top) * scale + anchorYOffset(sheet, frame, worldHeight)
        val dest = RectF(
            destLeft,
            destTop,
            destLeft + source.width() * scale,
            destTop + source.height() * scale
        )

        if (fighter.facing == FacingDirection.Left) {
            canvas.save()
            canvas.scale(-1f, 1f, fighter.x, fighter.groundY)
            canvas.drawBitmap(sheet.bitmap, frame.source, dest, paint)
            canvas.restore()
        } else {
            canvas.drawBitmap(sheet.bitmap, frame.source, dest, paint)
        }
    }

    private fun anchorYOffset(sheet: SpriteSheetAsset, frame: SheetFrame, worldHeight: Float): Float {
        return if (sheet.name == "EarthSmash" && frame.index >= sheet.frames.size - 3) {
            EarthSmashEndDrop * worldHeight / 720f
        } else {
            0f
        }
    }

    private fun drawKamehameha(canvas: Canvas, fighter: FighterRenderModel, worldHeight: Float) {
        val direction = if (fighter.facing == FacingDirection.Right) 1f else -1f
        val origin = kameOrigin(fighter, worldHeight)
        val progress = kameProgress(fighter)
        val worldScale = worldHeight / 720f
        val visualElapsed = fighter.pose.attackVisualElapsedSeconds

        when (fighter.pose.attackPhase) {
            "startup" -> Unit
            "active" -> {
                val start = assets.sheet("VFXKameStart")
                val mid = assets.sheet("VFXKameMid")
                val end = assets.sheet("VFXKameEnd")
                val activeTime = visualElapsed - AttackCatalog.kamehameha.startupDuration
                val beamLength = (300f + progress * 170f) * worldScale
                val beamThickness = (92f + progress * 36f) * worldScale
                val tipX = origin.x + direction * beamLength

                start?.let {
                    drawCentered(canvas, it, origin, worldScale * 0.22f, activeTime, direction)
                }
                mid?.let {
                    val left = min(origin.x, tipX)
                    val right = max(origin.x, tipX)
                    val rect = RectF(left, origin.y - beamThickness * 0.5f, right, origin.y + beamThickness * 0.5f)
                    drawStretched(canvas, it, rect, activeTime)
                }
                end?.let {
                    val tip = Vec2(tipX, origin.y)
                    drawCentered(canvas, it, tip, worldScale * 0.24f, activeTime, direction)
                }
            }
            "recovery" -> {
                val release = assets.sheet("VFXKameEnd") ?: return
                val recoveryTime = visualElapsed - AttackCatalog.kamehameha.startupDuration - AttackCatalog.kamehameha.activeDuration
                drawCentered(canvas, release, origin, worldScale * 0.22f, recoveryTime, direction)
            }
        }
    }

    private fun kameProgress(fighter: FighterRenderModel): Float {
        val clip = StickAnimationLibrary.clip(PoseLibrary.HEAVY_PUNCH)
        val duration = clip.durationSeconds.coerceAtLeast(0.0001f)
        return (fighter.pose.attackElapsedSeconds / duration).coerceIn(0f, 1f)
    }

    private fun kameOrigin(fighter: FighterRenderModel, worldHeight: Float): Vec2 {
        val hand = worldPoint(fighter)
        val scale = worldHeight / 720f
        val direction = if (fighter.facing == FacingDirection.Right) 1f else -1f
        return Vec2(
            hand.x + KameOriginForward * scale * direction,
            hand.y + KameOriginDrop * scale
        )
    }

    private fun worldPoint(fighter: FighterRenderModel): Vec2 {
        val frame = fighter.pose.frame
        val direction = if (fighter.facing == FacingDirection.Right) 1f else -1f
        val local = frame.leadHand + frame.rootOffset
        return Vec2(
            fighter.x + local.x * direction,
            fighter.groundY + local.y
        )
    }

    private fun drawCentered(
        canvas: Canvas,
        sheet: SpriteSheetAsset,
        center: Vec2,
        scale: Float,
        timeSeconds: Float,
        facing: Float
    ) {
        val frame = sheet.frameFor(timeSeconds)
        val source = frame.source
        val content = frame.contentBounds
        val destLeft = center.x - (content.exactCenterX() - source.left) * scale
        val destTop = center.y - (content.exactCenterY() - source.top) * scale
        val dest = RectF(
            destLeft,
            destTop,
            destLeft + source.width() * scale,
            destTop + source.height() * scale
        )
        if (facing < 0f) {
            canvas.save()
            canvas.scale(-1f, 1f, center.x, center.y)
            canvas.drawBitmap(sheet.bitmap, frame.source, dest, paint)
            canvas.restore()
        } else {
            canvas.drawBitmap(sheet.bitmap, frame.source, dest, paint)
        }
    }

    private fun drawStretched(
        canvas: Canvas,
        sheet: SpriteSheetAsset,
        dest: RectF,
        timeSeconds: Float
    ) {
        val frame = sheet.frameFor(timeSeconds)
        canvas.drawBitmap(sheet.bitmap, frame.source, dest, paint)
    }
}

private class SpriteAssetLibrary(context: Context) {
    private val sheets = linkedMapOf<String, SpriteSheetAsset>()

    init {
        val specs = listOf(
            SheetSpec("Idlespritesheet", 4, 2, 1.36f, true, 0.50f),
            SheetSpec("Walk", 3, 2, 0.72f, true, 0.50f),
            SheetSpec("JumpStart", 4, 2, 0.30f, false, 0.50f),
            SheetSpec("JumpRise", 4, 2, 0.72f, true, 0.50f),
            SheetSpec("Fall", 4, 2, 0.72f, true, 0.50f),
            SheetSpec("Land", 4, 2, 0.36f, false, 0.50f),
            SheetSpec("CrouchDown", 4, 2, 0.30f, false, 0.50f),
            SheetSpec("CrouchIdle", 4, 2, 0.90f, true, 0.50f),
            SheetSpec("CrouchUp", 4, 2, 0.30f, false, 0.50f),
            SheetSpec("Block", 4, 2, 0.42f, false, 0.50f),
            SheetSpec("BlockHit", 4, 2, 0.42f, false, 0.50f),
            SheetSpec("Jab", 4, 4, AttackCatalog.basicPunch.totalDuration, false, 0.48f),
            SheetSpec("Hurt", 4, 3, 0.52f, false, 0.50f),
            SheetSpec("KnockedOut", 4, 3, 1.12f, false, 0.50f),
            SheetSpec("KameCharge", 4, 2, KameChargeSeconds, false, 0.50f),
            SheetSpec("KameChargeRelease", 4, 2, AttackCatalog.kamehameha.startupDuration - KameChargeSeconds, false, 0.50f),
            SheetSpec("KameFire", 4, 2, AttackCatalog.kamehameha.activeDuration, true, 0.50f),
            SheetSpec("KameRelease", 4, 2, AttackCatalog.kamehameha.recoveryDuration, false, 0.50f),
            SheetSpec("EarthSmash", 4, 2, AttackCatalog.earthSmash.totalDuration, false, 0.50f),
            SheetSpec("BladeFlurry", 8, 4, AttackCatalog.bladeFlurry.totalDuration, false, 0.50f),
            SheetSpec("ElectroPulse", 4, 4, AttackCatalog.electroPulse.totalDuration, false, 0.50f),
            SheetSpec("FlameBurst", 8, 4, AttackCatalog.flameBurst.totalDuration, false, 0.50f),
            SheetSpec("VFXKameCharge", 4, 2, 0.72f, true, 0.58f),
            SheetSpec("VFXKameStart", 4, 2, 0.45f, true, 0.58f),
            SheetSpec("VFXKameMid", 4, 2, 0.54f, true, 0.58f),
            SheetSpec("VFXKameEnd", 4, 2, 0.45f, true, 0.58f),
            SheetSpec("VFXKameImpact", 3, 3, 0.45f, false, 0.58f)
        )
        for (spec in specs) {
            loadSheet(context, spec)?.let { sheets[spec.name] = it }
        }
    }

    fun sheet(name: String): SpriteSheetAsset? = sheets[name]

    private fun loadSheet(context: Context, spec: SheetSpec): SpriteSheetAsset? {
        val decoded = decodeSheet(context, spec.name) ?: return null

        val transparent = makeTransparent(decoded)
        val frames = buildFrames(transparent, spec.columns, spec.rows)
        val maxContentHeight = frames.maxOfOrNull { it.contentBounds.height() }?.toFloat() ?: spec.renderHeight
        return SpriteSheetAsset(
            spec.name,
            transparent,
            frames,
            spec.durationSeconds,
            spec.looping,
            spec.scale,
            spec.renderHeight,
            spec.renderWidthRatio,
            maxContentHeight
        )
    }

    private fun decodeSheet(context: Context, name: String): Bitmap? {
        for (extension in listOf("png", "jpg")) {
            val path = "spritesheets/$name.$extension"
            val decoded = try {
                context.assets.open(path).use { input ->
                    BitmapFactory.decodeStream(input, null, BitmapFactory.Options().apply {
                        inPreferredConfig = Bitmap.Config.ARGB_8888
                    })
                }
            } catch (_: IOException) {
                null
            }
            if (decoded != null) return decoded
        }
        return null
    }

    private fun makeTransparent(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        val key = averageCornerColor(pixels, width, height)
        val threshold = if (isMagenta(key)) 94 else 34
        for (index in pixels.indices) {
            val color = pixels[index]
            if ((color ushr 24) != 0 && isKeyColor(color, key, threshold)) {
                pixels[index] = 0
            }
        }

        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setHasAlpha(true)
            setPixels(pixels, 0, width, 0, 0, width, height)
        }
    }

    private fun buildFrames(bitmap: Bitmap, columns: Int, rows: Int): List<SheetFrame> {
        val frames = ArrayList<SheetFrame>(columns * rows)
        for (row in 0 until rows) {
            for (col in 0 until columns) {
                val left = col * bitmap.width / columns
                val top = row * bitmap.height / rows
                val right = (col + 1) * bitmap.width / columns
                val bottom = (row + 1) * bitmap.height / rows
                val source = Rect(left, top, right, bottom)
                frames += SheetFrame(frames.size, source, contentBounds(bitmap, source))
            }
        }
        return frames
    }

    private fun contentBounds(bitmap: Bitmap, source: Rect): Rect {
        val row = IntArray(source.width())
        var left = source.right
        var top = source.bottom
        var right = source.left - 1
        var bottom = source.top - 1
        for (y in source.top until source.bottom) {
            bitmap.getPixels(row, 0, source.width(), source.left, y, source.width(), 1)
            for (x in row.indices) {
                if ((row[x] ushr 24) != 0) {
                    val absoluteX = source.left + x
                    if (absoluteX < left) left = absoluteX
                    if (absoluteX > right) right = absoluteX
                    if (y < top) top = y
                    if (y > bottom) bottom = y
                }
            }
        }
        return if (right >= left && bottom >= top) {
            Rect(left, top, right + 1, bottom + 1)
        } else {
            source
        }
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

    private fun isKeyColor(color: Int, key: Int, threshold: Int): Boolean {
        return colorDistance(color, key) <= threshold || (isMagenta(key) && isMagentaArtifact(color))
    }

    private fun colorDistance(a: Int, b: Int): Int {
        val dr = abs(((a shr 16) and 0xFF) - ((b shr 16) and 0xFF))
        val dg = abs(((a shr 8) and 0xFF) - ((b shr 8) and 0xFF))
        val db = abs((a and 0xFF) - (b and 0xFF))
        return dr + dg + db
    }

    private fun isMagenta(color: Int): Boolean {
        val r = (color shr 16) and 0xFF
        val g = (color shr 8) and 0xFF
        val b = color and 0xFF
        return r >= 170 && g <= 115 && b >= 155
    }

    private fun isMagentaArtifact(color: Int): Boolean {
        val r = (color shr 16) and 0xFF
        val g = (color shr 8) and 0xFF
        val b = color and 0xFF
        return r >= 145 && b >= 145 && g <= 135 && r + b - g * 2 >= 180
    }
}

private data class SheetSelection(
    val name: String,
    val timeSeconds: Float
)

private data class SheetSpec(
    val name: String,
    val columns: Int,
    val rows: Int,
    val durationSeconds: Float,
    val looping: Boolean,
    val scale: Float,
    val renderHeight: Float = FighterRenderHeight,
    val renderWidthRatio: Float = FighterRenderWidthRatio
)

private data class SpriteSheetAsset(
    val name: String,
    val bitmap: Bitmap,
    val frames: List<SheetFrame>,
    val durationSeconds: Float,
    val looping: Boolean,
    val scale: Float,
    val renderHeight: Float,
    val renderWidthRatio: Float,
    val maxContentHeight: Float
) {
    fun frameFor(timeSeconds: Float): SheetFrame {
        if (frames.size == 1 || durationSeconds <= 0f) {
            return frames.first()
        }
        val t = if (looping) {
            timeSeconds % durationSeconds
        } else {
            timeSeconds.coerceIn(0f, durationSeconds)
        }
        val frameDuration = durationSeconds / frames.size.toFloat()
        val index = (t / frameDuration).toInt().coerceIn(0, frames.lastIndex)
        return frames[index]
    }
}

data class SheetFrame(
    val index: Int,
    val source: Rect,
    val contentBounds: Rect = source
)
