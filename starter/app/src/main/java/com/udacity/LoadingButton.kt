package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator()
    private var animatedWidth = 0.0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 40.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (buttonState) {
            ButtonState.Loading -> setAnimator()
            ButtonState.Completed -> stopAnimator()
        }
    }

    private fun stopAnimator() {
        valueAnimator.cancel()
        invalidate()
    }


    init {

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.color = Color.CYAN

        canvas?.drawRect(
            0.0f,
            0.0f,
            width.toFloat(),
            height.toFloat(),
            paint
        )

        when (buttonState) {
            ButtonState.Loading -> setLoadingAnimation(canvas)
            ButtonState.Completed -> setDefaultButtonState(canvas)
        }

    }

    private fun setLoadingAnimation(canvas: Canvas?) {
        paint.color = Color.BLUE
        canvas?.drawRect(
            0.0f,
            0.0f,
            animatedWidth,
            height.toFloat(),
            paint
        )

        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.CENTER
        canvas?.drawText(
            context.getString(R.string.button_loading),
            width / 2.0f, height / 2.0f, paint
        )

    }

    private fun setDefaultButtonState(canvas: Canvas?) {
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.CENTER
        canvas?.drawText(
            context.getString(R.string.button_text_download),
            width / 2.0f, height / 2.0f, paint
        )

    }


    private fun setAnimator() {

        valueAnimator.duration = 2500
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.setFloatValues(0.0f, width.toFloat())
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.addUpdateListener {
            animatedWidth = it.animatedValue as Float
            invalidate()
        }
        valueAnimator.start()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}