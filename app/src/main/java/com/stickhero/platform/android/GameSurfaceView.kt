package com.stickhero.platform.android

import android.content.Context
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.stickhero.game.core.GameConfig
import com.stickhero.game.core.StickHeroGame

class GameSurfaceView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    private val game = StickHeroGame(GameConfig.default())
    private val inputMapper = AndroidTouchInputMapper()
    private val renderer = CanvasGameRenderer(inputMapper)
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
        gameLoop = AndroidGameLoop(holder, game, renderer) { inputMapper.currentInput() }
        gameLoop?.startLoop()
    }

    private fun stopLoop() {
        gameLoop?.stopLoop()
        gameLoop = null
    }
}
