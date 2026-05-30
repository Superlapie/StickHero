package com.stickhero.platform.android

import android.content.Context
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.stickhero.game.core.GameMode

class GameSurfaceView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    private val app = StickHeroApp()
    private val inputMapper = AndroidTouchInputMapper()
    private val renderer = CanvasGameRenderer(context, inputMapper)
    private var gameLoop: AndroidGameLoop? = null
    private var hasSurface = false

    init {
        holder.addCallback(this)
        isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        hasSurface = true
        startLoop()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) = Unit

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        hasSurface = false
        stopLoop()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (app.isMenu()) {
            if (event.actionMasked == MotionEvent.ACTION_UP) {
                val x = event.x
                val y = event.y
                when {
                    MenuLayout.startBounds(width, height).contains(x, y) -> {
                        app.start(GameMode.Normal)
                        inputMapper.reset()
                    }
                    MenuLayout.debugBounds(width, height).contains(x, y) -> {
                        app.start(GameMode.DebugSandbox)
                        inputMapper.reset()
                    }
                }
            }
            return true
        }
        inputMapper.onTouch(event, width, height)
        return true
    }

    fun resume() {
        if (hasSurface) startLoop()
    }

    fun pause() {
        stopLoop()
    }

    private fun startLoop() {
        if (gameLoop != null) return
        gameLoop = AndroidGameLoop(holder, app, renderer) { inputMapper.currentInput() }
        gameLoop?.startLoop()
    }

    private fun stopLoop() {
        gameLoop?.stopLoop()
        gameLoop = null
    }
}
