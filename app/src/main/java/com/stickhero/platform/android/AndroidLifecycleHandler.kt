package com.stickhero.platform.android

class AndroidLifecycleHandler(private val gameSurfaceView: GameSurfaceView) {
    fun onResume() {
        gameSurfaceView.resume()
    }

    fun onPause() {
        gameSurfaceView.pause()
    }
}
