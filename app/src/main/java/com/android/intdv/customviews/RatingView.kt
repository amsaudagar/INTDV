package com.android.intdv.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

/**
 * Creates the circular progress view to display movie rating
 */
class RatingView : View {

    private var baseColor: Int = Color.WHITE
    private var progressColor: Int = Color.GREEN

    private var rating: Float = 0f

    private val path = Path()
    private val paint = Paint()
    private val oval = RectF()
    private val paint2 = Paint()

    companion object {
        private const val FULL_CIRCLE = 360f
        private const val START_ANGLE = -90f
        private const val STROKE_WIDTH = 10.0f
        private const val TOTAL_PERCENTAGE = 100.0f
        const val SPACE_OFFSET = 20
    }

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
        defStyleAttr) {
        initialize()
    }

    fun setDetails(baseColor: Int, progressColor: Int, value: Float) {
        this.baseColor = baseColor
        this.progressColor = progressColor
        this.rating = value
    }

    /**
     * Initializes the resources
     */
    private fun initialize() {}

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        val radius = if (width > height) height / 2
        else width / 2

        path.addCircle(width / 2, height / 2, radius, Path.Direction.CW)

        paint.color = baseColor
        paint.strokeWidth = STROKE_WIDTH

        val centerX: Float
        val centerY: Float
        paint.style = Paint.Style.STROKE

        centerX = width / 2
        centerY = height / 2

        // Take 20 offset to provide side margin
        oval.set(centerX - radius + SPACE_OFFSET,
            centerY - radius + SPACE_OFFSET,
            centerX + radius - SPACE_OFFSET,
            centerY + radius - SPACE_OFFSET)

        canvas.drawArc(oval, 0f, FULL_CIRCLE, false, paint)


        paint2.color = progressColor
        paint2.strokeWidth = STROKE_WIDTH
        paint2.style = Paint.Style.STROKE
        canvas.drawArc(oval, START_ANGLE, getSweepAngle(), false, paint2)
    }

    /**
     * Calculates and returns the sweep angle
     */
    private fun getSweepAngle(): Float {
        return FULL_CIRCLE * rating / TOTAL_PERCENTAGE
    }
}