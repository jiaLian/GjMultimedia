package com.goodjia.multimedia.fragment.component

import android.net.Uri
import android.os.Bundle
import android.view.animation.Animation
import com.goodjia.multimedia.MediaController
import com.goodjia.multimedia.Task
import com.goodjia.multimedia.fragment.BaseFragment

abstract class MediaFragment : BaseFragment(), MediaController {
    companion object {
        const val KEY_REPEAT_TIMES = "repeat_times"
        const val KEY_PLAY_TIME = "play_time"
        const val KEY_URI = "uri"
    }

    protected val mediaCallback: MediaCallback? by lazy {
        try {
            parentFragment as MediaCallback?
        } catch (e: Exception) {
            null
        }
    }
    protected val animationCallback: AnimationCallback? by lazy {
        try {
            parentFragment as AnimationCallback?
        } catch (e: Exception) {
            null
        }
    }
    protected var uri: Uri? = null

    protected var playTime: Int = Task.DEFAULT_PLAYTIME
    protected var resetPlayTime: Int = Task.DEFAULT_PLAYTIME
    protected var repeatTimes: Int = 1
    protected var repeatCount: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            playTime = arguments?.getInt(KEY_PLAY_TIME) ?: Task.DEFAULT_PLAYTIME
            resetPlayTime = arguments?.getInt(KEY_PLAY_TIME) ?: Task.DEFAULT_PLAYTIME
            repeatTimes = arguments?.getInt(KEY_REPEAT_TIMES) ?: Int.MIN_VALUE
        } else {
            playTime = savedInstanceState.getInt(KEY_PLAY_TIME, Task.DEFAULT_PLAYTIME)
            resetPlayTime = savedInstanceState.getInt(KEY_PLAY_TIME, Task.DEFAULT_PLAYTIME)
            repeatTimes = savedInstanceState.getInt(KEY_REPEAT_TIMES)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_REPEAT_TIMES, repeatTimes)
        outState.putInt(KEY_PLAY_TIME, playTime)
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (!isDetached) animationCallback?.animation(transit, enter, nextAnim)
            ?: super.onCreateAnimation(
                transit,
                enter,
                nextAnim
            ) else null
    }

    override fun repeat() {
        repeatCount = 0
        playTime = resetPlayTime
    }
    override fun setVolume(volumePercent: Int) {
    }

    interface AnimationCallback {
        fun animation(transit: Int, enter: Boolean, nextAnim: Int): Animation?
    }

    interface MediaCallback {
        fun onCompletion(action: Int, message: String?)

        fun onError(action: Int, message: String?)

        fun onPrepared()
    }
}
