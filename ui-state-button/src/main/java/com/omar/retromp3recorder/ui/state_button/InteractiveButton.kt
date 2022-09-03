package com.omar.retromp3recorder.ui.state_button

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import io.reactivex.rxjava3.disposables.CompositeDisposable


open class InteractiveButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val imageView: ImageView
        get() = findViewById(R.id.vsb_image_view)

    private var currentState: State? = null

    protected val compositeDisposable = CompositeDisposable()

    init {
        View.inflate(context, R.layout.view_state_button, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InteractiveButton, 0, 0)
        try {
            typedArray.getDrawable(R.styleable.InteractiveButton_image)
                ?.let { imageView.setImageDrawable(it) }
        } finally {
            typedArray.recycle()
        }
        onState(State.ENABLED)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        isClickable = enabled
        isFocusable
    }

    fun onState(state: State) {
        if (currentState == state) return
        currentState = state
        when (state) {
            State.ENABLED -> onEnabled()
            State.DISABLED -> onDisabled()
            State.RUNNING -> onRunning()
        }
    }

    private fun onEnabled() {
        compositeDisposable.clear()
        alpha = 1.0f
        isEnabled = true
        isActivated = false
    }

    private fun onDisabled() {
        compositeDisposable.clear()
        alpha = 0.30f
        isEnabled = false
        isActivated = false
    }

    open fun onRunning() {
        compositeDisposable.clear()
        alpha = 1.0f
        isActivated = true
        isEnabled = false
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        compositeDisposable.clear()
    }

    enum class State {
        ENABLED,
        DISABLED,
        RUNNING
    }
}

