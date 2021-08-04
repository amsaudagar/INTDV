package com.android.intdv.customviews

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.android.intdv.R
import kotlinx.android.synthetic.main.circular_progress_widget_layout.view.*

/**
 * Represents the circular progress view for rating details
 */
class CircularProgressBar : LinearLayout {

    private lateinit var progressBar: RatingView

    private var baseColor: Int = Color.WHITE
    private var progressColor: Int = Color.GREEN

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,
            defStyleAttr) {
        initView(context, attrs)
    }

    /**
     * Initialize the views
     *
     * @param context - view context
     * @param attrs - view attribute set
     */
    private fun initView(context: Context, attrs: AttributeSet?) {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rootView = inflater.inflate(R.layout.circular_progress_widget_layout, this, true)
        progressBar = rootView.findViewById(R.id.circularProgressBar)

        setViewAttribute(context, attrs)
    }

    /**
     * Sets the view attributes
     */
    private fun setViewAttribute(context: Context?, attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context?.obtainStyledAttributes(attrs, R.styleable.CircularProgressBar)
            baseColor = typedArray?.getInt(R.styleable.CircularProgressBar_baseColor, Color.GREEN)
                    ?: Color.GREEN
            progressColor = typedArray?.getInt(R.styleable.CircularProgressBar_progressColor, Color.WHITE)
                    ?: Color.WHITE

            typedArray?.recycle()
        }
    }


    /**
     * Sets the details to update the view
     *
     * @param rating - rating
     * @param baseColor - color to display the progress base color
     * @param progressColor - color to display the progress bar color
     */
    fun setDetails(rating: Float, baseColor: Int, progressColor: Int) {
        txtTitle?.text = rating.toString().substringBefore(".")

        if (baseColor != -1) {
            this.baseColor = baseColor
        }

        if (progressColor != -1) {
            this.progressColor = progressColor
        }

        progressBar.setDetails(baseColor, progressColor, rating)
    }
}