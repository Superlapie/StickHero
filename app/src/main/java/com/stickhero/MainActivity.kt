package com.stickhero

import android.app.Activity
import android.os.Bundle
import com.stickhero.platform.android.AndroidLifecycleHandler
import com.stickhero.platform.android.GameSurfaceView

class MainActivity : Activity() {
    private lateinit var lifecycleHandler: AndroidLifecycleHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameView = GameSurfaceView(this)
        lifecycleHandler = AndroidLifecycleHandler(gameView)
        setContentView(gameView)
    }

    override fun onResume() {
        super.onResume()
        lifecycleHandler.onResume()
    }

    override fun onPause() {
        lifecycleHandler.onPause()
        super.onPause()
    }
}
