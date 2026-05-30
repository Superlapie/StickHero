package com.stickhero.platform.android

import android.view.SurfaceHolder
import com.stickhero.game.input.InputState
import com.stickhero.game.util.Time

class AndroidGameLoop(
    private val holder: SurfaceHolder,
    private val app: StickHeroApp,
    private val renderer: CanvasGameRenderer,
    private val inputProvider: () -> InputState
) : Thread("StickHeroGameLoop") {
    @Volatile private var running = false
    private var lastNanos = System.nanoTime()

    fun startLoop() {
        running = true
        lastNanos = System.nanoTime()
        start()
    }

    fun stopLoop() {
        running = false
        join(600)
    }

    override fun run() {
        while (running) {
            val now = System.nanoTime()
            val delta = Time.secondsFromNanos(now - lastNanos).coerceAtMost(0.05f)
            lastNanos = now

            val frame = app.update(delta, inputProvider())
            val canvas = holder.lockCanvas()
            if (canvas != null) {
                try {
                    renderer.draw(canvas, frame)
                } finally {
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }
}
