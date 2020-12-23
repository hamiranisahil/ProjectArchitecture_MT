package com.example.library.textview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import com.example.library.R
import java.math.BigDecimal


class MyTextView : androidx.appcompat.widget.AppCompatTextView {

    private val DEFAULT_TEXT_SIZE: Int = 14

    constructor(context: Context) : super(context) {
        initialization(null)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initialization(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        initialization(attributeSet)
    }

    @SuppressLint("ResourceType")
    private fun initialization(attributeSet: AttributeSet?) {
        if (attributeSet != null) {
            val typedArray =
                context.obtainStyledAttributes(attributeSet, R.styleable.MyTextView)
            // Handle Default
            val set: IntArray = intArrayOf(android.R.attr.textSize)
            val defaultTypedArray = context.obtainStyledAttributes(attributeSet, set)

//            val text = defaultTypedArray.getText(0)
//            val textSize = defaultTypedArray.getDimensionPixelSize(0, DEFAULT_TEXT_SIZE)

            // Handle TextSize
            val textSize = typedArray.getInt(R.styleable.MyTextView_text_size, DEFAULT_TEXT_SIZE)
            val finalTextSize =
                (textSize * (resources.displayMetrics.widthPixels.toDouble() / 720)).toFloat()
//            val finalTextSize = (textSize / resources.displayMetrics.scaledDensity.toDouble() + 0.5f).toFloat()
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, finalTextSize)

            // Handle Double Precision
            val precisionValue = typedArray.getInt(R.styleable.MyTextView_precision_value, -1)
            if (text != null && precisionValue != -1) {
                this.text = truncateDecimal(text.toString().toDouble(), precisionValue).toString()
            }

            typedArray.recycle()
            defaultTypedArray.recycle()
        }
    }


    private fun truncateDecimal(x: Double, numberofDecimals: Int): BigDecimal? {
        return if (x > 0) {
            BigDecimal(x.toString()).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR)
        } else {
            BigDecimal(x.toString()).setScale(numberofDecimals, BigDecimal.ROUND_CEILING)
        }
    }
}