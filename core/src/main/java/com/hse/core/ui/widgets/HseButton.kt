package com.hse.core.ui.widgets

import android.content.Context
import android.text.Editable
import android.text.Spannable
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import com.hse.core.R
import com.hse.core.common.color
import com.hse.core.common.dip
import com.hse.core.utils.Fonts


class HseButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    TextView(context, attrs, defStyleAttr) {

    private val drawables = arrayOf(0, 0)

    init {
        typeface = Fonts.futuraMedium
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        setPadding(dip(24f), 0, dip(24f), 0)
        gravity = Gravity.CENTER
        minHeight = dip(36f)
        minimumHeight = dip(36f)

        val a = getContext().obtainStyledAttributes(attrs, R.styleable.HseButton)
        val ordinal = a.getInt(R.styleable.HseButton_type, 0)
        val buttonType = Type.values()[ordinal]
        setType(buttonType)
    }

    fun setType(type: Type) {
        when (type) {
            Type.BLUE -> {
                setBackgroundResource(R.drawable.hse_button_selector_blue)
                setTextColor(color(R.color.buttonNormalText))
            }
            Type.TRANSPARENT -> {
                setBackgroundResource(R.drawable.hse_button_selector_transparent)
                setTextColor(color(R.color.buttonTransparentText))
            }
            Type.TRANSPARENT_WITH_BORDER -> {
                setBackgroundResource(R.drawable.hse_button_selector_transparent_w_border)
                setTextColor(color(R.color.buttonTransparentText))
            }
        }
    }

    fun setImage(start: Int, end: Int) {
        drawables[0] = start
        drawables[1] = end
        text = text
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(getSpannableText(text), BufferType.SPANNABLE)
    }

    private fun getSpannableText(text: CharSequence?): Spannable? {
        if (text == null) return null
        var startDrawables = 0
        var endDrawables = 0
        if (text is Spannable) {
            text.getSpans(0, text.length, ImageSpanExt::class.java)?.forEach {
                if (it.tag == 0) startDrawables++
                if (it.tag == 1) endDrawables++
                text.removeSpan(it)
            }
        }
        val editable = Editable.Factory.getInstance().newEditable(text)
        if (startDrawables > 0) for (i in 0 until startDrawables) editable.replace(0, 2, "")
        if (endDrawables > 0) for (i in 0 until endDrawables) editable.replace(
            editable.length - 2,
            editable.length,
            ""
        )
        if (drawables != null) { // lie, not always true
            if (drawables[0] != 0) editable.insert(0, "  ")
            if (drawables[1] != 0) editable.insert(editable.length, "  ")
        }
        val spannable = Spannable.Factory.getInstance().newSpannable(editable)
        if (drawables != null) {
            if (drawables[0] != 0) {
                spannable.setSpan(
                    ImageSpanExt(context, drawables[0], 0),
                    0,
                    1,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE
                )
            }
        }
        return spannable
    }

    enum class Type {
        BLUE, TRANSPARENT, TRANSPARENT_WITH_BORDER
    }

    inner class ImageSpanExt(context: Context, drawable: Int, val tag: Int) :
        ImageSpan(context, drawable)

}