package com.stickhero.platform.android

import android.graphics.RectF

object MenuLayout {
    fun startBounds(width: Int, height: Int): RectF {
        return buttonBounds(width, height, index = 0)
    }

    fun debugBounds(width: Int, height: Int): RectF {
        return buttonBounds(width, height, index = 1)
    }

    private fun buttonBounds(width: Int, height: Int, index: Int): RectF {
        val buttonWidth = width * 0.34f
        val buttonHeight = height * 0.11f
        val spacing = height * 0.025f
        val totalHeight = buttonHeight * 2f + spacing
        val top = height * 0.54f - totalHeight / 2f
        val left = width / 2f - buttonWidth / 2f
        return RectF(
            left,
            top + index * (buttonHeight + spacing),
            left + buttonWidth,
            top + index * (buttonHeight + spacing) + buttonHeight
        )
    }
}
