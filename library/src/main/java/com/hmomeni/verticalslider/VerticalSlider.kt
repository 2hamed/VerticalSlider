package com.hmomeni.verticalslider

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.concurrent.thread

class VerticalSlider : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        val a = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.VerticalSlider,
            0, 0
        )

        try {
            val iconLoResId = a.getResourceId(R.styleable.VerticalSlider_vs_iconLow, -1)
            val iconMedResId = a.getResourceId(R.styleable.VerticalSlider_vs_iconMedium, -1)
            val iconHiResId = a.getResourceId(R.styleable.VerticalSlider_vs_iconHigh, -1)
            max = a.getInteger(R.styleable.VerticalSlider_vs_max, max)
            progress = a.getInteger(R.styleable.VerticalSlider_vs_progress, progress)
            thread {
                if (iconHiResId != -1)
                    hiIcon = getBitmapFromVectorDrawable(context, iconHiResId)
                if (iconMedResId != -1)
                    midIcon = getBitmapFromVectorDrawable(context, iconMedResId)
                if (iconLoResId != -1)
                    lowIcon = getBitmapFromVectorDrawable(context, iconLoResId)
            }
        } finally {
            a.recycle()
        }
    }

    var hiIcon: Bitmap? = null
    var midIcon: Bitmap? = null
    var lowIcon: Bitmap? = null

    private val iconWidth = dpToPx(36)
    private val iconRect: RectF = RectF()
    private val layoutRect: RectF = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    private val layoutPaint = Paint().apply {
        color = Color.parseColor("#aa787878")
        isAntiAlias = true
    }
    private val progressRect: RectF = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    private val progressPaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
    }
    private val radius = dpToPx(15).toFloat()

    private val path = Path()

    var onProgressChangeListener: OnSliderProgressChangeListener? = null
    var max: Int = 10
    var progress: Int = 5
        set(value) {
            if (value > max) {
                throw RuntimeException("progress must not be larger than max")
            }
            field = value
            onProgressChangeListener?.onChanged(progress, max)
            progressRect.set(
                0f,
                (1 - calculateProgress()) * measuredHeight,
                measuredWidth.toFloat(),
                measuredHeight.toFloat()
            )
            invalidate()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (measuredHeight > 0 && measuredWidth > 0) {
            layoutRect.set(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
            progressRect.set(
                0f,
                (1 - calculateProgress()) * measuredHeight,
                measuredWidth.toFloat(),
                measuredHeight.toFloat()
            )
            iconRect.set(
                measuredWidth / 2f - iconWidth / 2,
                measuredHeight / 2f - iconWidth / 2,
                measuredWidth / 2f + iconWidth / 2,
                measuredHeight / 2f + iconWidth / 2
            )
            path.addRoundRect(layoutRect, radius, radius, Path.Direction.CW)
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.clipPath(path)
        canvas.drawRect(layoutRect, layoutPaint)
        canvas.drawRect(progressRect, progressPaint)

        if (lowIcon != null && midIcon != null && hiIcon != null) {
            when {
                progress < max / 3 -> {
                    canvas.drawBitmap(lowIcon, null, iconRect, null)
                }
                progress < max * 2 / 3 -> {
                    canvas.drawBitmap(midIcon, null, iconRect, null)
                }
                else -> {
                    canvas.drawBitmap(hiIcon, null, iconRect, null)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                val y = event.y
                val currentHeight = measuredHeight - y
                val percent = currentHeight / measuredHeight.toFloat()
                progress = when {
                    percent >= 1 -> max
                    percent <= 0 -> 0
                    else -> (max * percent).toInt()
                }
                return true
            }
        }
        return false
    }

    private fun calculateProgress(): Float {
        return progress.toFloat() / max.toFloat()
    }

    interface OnSliderProgressChangeListener {
        fun onChanged(progress: Int, max: Int)
    }

    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)

        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    private fun dpToPx(dp: Int) = (dp * resources.displayMetrics.density).toInt()

}