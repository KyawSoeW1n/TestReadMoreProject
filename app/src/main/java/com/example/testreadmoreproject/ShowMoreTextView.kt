package com.example.testreadmoreproject

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatTextView

class ShowMoreTextView : AppCompatTextView {
    private var showingLine = 1
    private var showingChar = 0
    private var isCharEnable = false
    private var isClickEnable = false
    private var showMore = "Show more"
    private var showLess = "Show less"
    private val ellipse = "..."
    private val magicNumber = 5
    private var isExpanded = true
    private lateinit var onClickSeeMore: OnClickSeeMore

    private var showMoreTextColor = Color.BLACK
    private var showLessTextColor = Color.BLACK
    private var mainText: String? = null
    private var isAlreadySet = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onFinishInflate() {
        super.onFinishInflate()
        mainText = text.toString()
    }

    private fun addShowMore() {
        val vto = viewTreeObserver
        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val text = text.toString()
                if (!isAlreadySet) {
                    mainText = getText().toString()
                    isAlreadySet = true
                }
                var showingText = ""
                if (isCharEnable) {
                    if (showingChar >= text.length) {
                        try {
                            throw Exception("Character count cannot be exceed total line count")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    var newText = text.substring(0, showingChar)
                    newText += ellipse + showMore
                    isExpanded = true
                    setText(newText)
                    Log.e("Text", newText)
                } else {
                    if (showingLine >= lineCount) {
                        try {
                            throw Exception("Line Number cannot be exceed total line count")
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.e(TAG, "Error: " + e.message)
                        }
                        viewTreeObserver.removeOnGlobalLayoutListener(this)
                        return
                    }
                    var start = 0
                    var end: Int
                    for (i in 0 until showingLine) {
                        end = layout.getLineEnd(i)
                        showingText += text.substring(start, end)
                        start = end
                    }
                    var newText = showingText.substring(
                        0,
                        showingText.length - (ellipse.length + showMore.length + magicNumber)
                    )
                    Log.e("Text", newText)
                    Log.e("Text", showingText)
                    newText += ellipse + showMore
                    isExpanded = true
                    setText(newText)
                }
                setShowMoreColoringAndClickable()
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun setShowMoreColoringAndClickable() {
        val spannableString = SpannableString(text)
        Log.d(TAG, "Text: $text")
        spannableString.setSpan(
            object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                }

                override fun onClick(@Nullable view: View) {
//                    maxLines = Int.MAX_VALUE
//                    text = mainText
//                    isExpanded = false
//                    showLessButton()
//                    onClickSeeMore.clickSeeMore()
//                    Log.e("Item Clicked ", "$mainText")
                }
            },
            text.length - (ellipse.length + showMore.length),
            text.length, 0
        )
        spannableString.setSpan(
            ForegroundColorSpan(showMoreTextColor),
            text.length - (ellipse.length + showMore.length),
            text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        movementMethod = LinkMovementMethod.getInstance()
        setText(spannableString, BufferType.SPANNABLE)
    }

    private fun showLessButton() {
        val text = text.toString() + ellipse + showLess
        val spannableString = SpannableString(text)
        spannableString.setSpan(
            object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                }

                override fun onClick(@Nullable view: View) {
                    maxLines = showingLine
                    addShowMore()
                }
            },
            text.length - (ellipse.length + showLess.length),
            text.length, 0
        )
        spannableString.setSpan(
            ForegroundColorSpan(showLessTextColor),
            text.length - (ellipse.length + showLess.length),
            text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        movementMethod = LinkMovementMethod.getInstance()
        setText(spannableString, BufferType.SPANNABLE)
    }
    /*
     * User added field
     * */
    /**
     * User can add minimum line number to show collapse text
     *
     * @param lineNumber int
     */
    fun setShowingLine(lineNumber: Int) {
        if (lineNumber == 0) {
            try {
                throw Exception("Line Number cannot be 0")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return
        }
        isCharEnable = false
        showingLine = lineNumber
        maxLines = showingLine
        Log.e("FUCKING SHIT EXPANED", "$isExpanded")
        if (isExpanded) {
            Log.e("FUCKING SHIT 1", "1")
            addShowMore()
        } else {
            Log.e("FUCKING SHIT 2", "2")
            maxLines = Int.MAX_VALUE
            showLessButton()
        }
    }

    /**
     * User can limit character limit of text
     *
     * @param character int
     */
    fun setShowingChar(character: Int) {
        if (character == 0) {
            try {
                throw java.lang.Exception("Character length cannot be 0")
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return
        }
        isCharEnable = true
        showingChar = character
        if (isExpanded) {
            addShowMore()
        } else {
            maxLines = Int.MAX_VALUE
            showLessButton()
        }
    }

    fun setSeeMoreClickEnable(isClickEnable: Boolean) {
        this.isClickEnable = isClickEnable
    }

    fun setSeeMoreListener(onClickSeeMore: OnClickSeeMore) {
        this.onClickSeeMore = onClickSeeMore
    }

    fun setExpanded(isExpanded: Boolean) {
        this.isExpanded = isExpanded
        if (!this.isExpanded)
            addShowMore()
    }

    companion object {
        private val TAG = ShowMoreTextView::class.java.name
    }

//    object SaveState {
//        var isCollapse = true
//    }
}