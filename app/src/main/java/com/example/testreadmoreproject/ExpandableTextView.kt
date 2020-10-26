package com.example.testreadmoreproject

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView


/**
 * Copyright (C) 2017 Cliff Ophalvens (Blogc.at)
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Cliff Ophalvens (Blogc.at)
 */
class ExpandableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) :
    TextView(context, attrs, defStyle) {
    /**
     * Returns the [OnExpandListener].
     * @return the listener.
     */
    /**
     * Sets a listener which receives updates about this [ExpandableTextView].
     * @param onExpandListener the listener.
     */
    var onExpandListener: OnExpandListener? = null
    /**
     * Returns the current [TimeInterpolator] for expanding.
     * @return the current interpolator, null by default.
     */
    /**
     * Sets a [TimeInterpolator] for expanding.
     * @param expandInterpolator the interpolator
     */
    var expandInterpolator: TimeInterpolator
    /**
     * Returns the current [TimeInterpolator] for collapsing.
     * @return the current interpolator, null by default.
     */
    /**
     * Sets a [TimeInterpolator] for collpasing.
     * @param collapseInterpolator the interpolator
     */
    var collapseInterpolator: TimeInterpolator
    private var maxLines: Int
    private var animationDuration: Long
    private var animating = false

    /**
     * Is this [ExpandableTextView] expanded or not?
     * @return true if expanded, false if collapsed.
     */
    var isExpanded = false
        private set
    private var collapsedHeight = 0
    override fun getMaxLines(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            super.getMaxLines()
        } else try {
            val mMaxMode = TextView::class.java.getField("mMaxMode")
            mMaxMode.isAccessible = true
            val mMaximum = TextView::class.java.getField("mMaximum")
            mMaximum.isAccessible = true
            val mMaxModeValue = mMaxMode[this] as Int
            val mMaximumValue = mMaximum[this] as Int
            if (mMaxModeValue == MAXMODE_LINES) mMaximumValue else -1
        } catch (e: Exception) {
            -1
        }
    }

    override fun setMaxLines(maxLines: Int) {
        super.setMaxLines(maxLines)
        this.maxLines = maxLines
    }

    /**
     * Toggle the expanded state of this [ExpandableTextView].
     * @return true if toggled, false otherwise.
     */
    fun toggle(): Boolean {
        return if (isExpanded) collapse() else expand()
    }

    /**
     * Expand this [ExpandableTextView].
     * @return true if expanded, false otherwise.
     */
    fun expand(): Boolean {
        if (!isExpanded && !animating && maxLines >= 0) {
            animating = true

            // notify listener
            if (onExpandListener != null) {
                onExpandListener!!.onExpand(this)
            }

            // get collapsed height
            measure(
                MeasureSpec.makeMeasureSpec(this.measuredWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
            collapsedHeight = this.measuredHeight

            // set maxLines to MAX Integer, so we can calculate the expanded height
            super.setMaxLines(Int.MAX_VALUE)
            // get expanded height
            measure(
                MeasureSpec.makeMeasureSpec(this.measuredWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
            val expandedHeight = this.measuredHeight

            // animate from collapsed height to expanded height
            val valueAnimator = ValueAnimator.ofInt(collapsedHeight, expandedHeight)
            valueAnimator.addUpdateListener { animation ->
                var layoutParams = this@ExpandableTextView.layoutParams
                layoutParams.height = animation.animatedValue as Int
                layoutParams = layoutParams
            }
            valueAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    // if fully expanded, set height to WRAP_CONTENT, because when rotating the device
                    // the height calculated with this ValueAnimator isn't correct anymore
                    var layoutParams = this@ExpandableTextView.layoutParams
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    layoutParams = layoutParams

                    // keep track of current status
                    isExpanded = true
                    animating = false
                }
            })

            // set interpolator
            valueAnimator.interpolator = expandInterpolator

            // start the animation
            valueAnimator
                .setDuration(animationDuration)
                .start()
            return true
        }
        return false
    }

    /**
     * Collapse this [TextView].
     * @return true if collapsed, false otherwise.
     */
    fun collapse(): Boolean {
        if (isExpanded && !animating && maxLines >= 0) {
            animating = true

            // notify listener
            if (onExpandListener != null) {
                onExpandListener!!.onCollapse(this)
            }

            // get expanded height
            val expandedHeight = this.measuredHeight

            // animate from expanded height to collapsed height
            val valueAnimator = ValueAnimator.ofInt(expandedHeight, collapsedHeight)
            valueAnimator.addUpdateListener { animation ->
                var layoutParams = this@ExpandableTextView.layoutParams
                layoutParams.height = animation.animatedValue as Int
                layoutParams = layoutParams
            }
            valueAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    // set maxLines to original value
                    super@ExpandableTextView.setMaxLines(maxLines)

                    // if fully collapsed, set height to WRAP_CONTENT, because when rotating the device
                    // the height calculated with this ValueAnimator isn't correct anymore
                    var layoutParams = this@ExpandableTextView.layoutParams
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    layoutParams = layoutParams

                    // keep track of current status
                    isExpanded = false
                    animating = false
                }
            })

            // set interpolator
            valueAnimator.interpolator = collapseInterpolator

            // start the animation
            valueAnimator
                .setDuration(animationDuration)
                .start()
            return true
        }
        return false
    }

    /**
     * Sets the duration of the expand / collapse animation.
     * @param animationDuration duration in milliseconds.
     */
    fun setAnimationDuration(animationDuration: Long) {
        this.animationDuration = animationDuration
    }

    /**
     * Sets a [TimeInterpolator] for expanding and collapsing.
     * @param interpolator the interpolator
     */
    fun setInterpolator(interpolator: TimeInterpolator) {
        expandInterpolator = interpolator
        collapseInterpolator = interpolator
    }

    /**
     * Interface definition for a callback to be invoked when
     * a [ExpandableTextView] is expanded or collapsed.
     */
    interface OnExpandListener {
        /**
         * The [ExpandableTextView] is being expanded.
         * @param view the textview
         */
        fun onExpand(view: ExpandableTextView?)

        /**
         * The [ExpandableTextView] is being collapsed.
         * @param view the textview
         */
        fun onCollapse(view: ExpandableTextView?)
    }

    companion object {
        // copy off TextView.LINES
        private const val MAXMODE_LINES = 1
    }

    init {

        // read attributes
        val attributes =
            context.obtainStyledAttributes(attrs, R.styleable.ExpandableTextView, defStyle, 0)
        animationDuration = 0.toLong()
        attributes.recycle()

        // keep the original value of maxLines
        maxLines = getMaxLines()

        // create default interpolators
        expandInterpolator = AccelerateDecelerateInterpolator()
        collapseInterpolator = AccelerateDecelerateInterpolator()
    }
}