package com.revolgenx.anilib.ui.view.widgets

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.widget.doOnTextChanged
import com.revolgenx.anilib.R
import com.revolgenx.anilib.databinding.AlCountEditTextLayoutBinding

class AlCountEditTextLayout : AlCardView {

    var onCountChangeListener: ((count: Double) -> Unit)? = null
    var inputType = InputType.TYPE_CLASS_NUMBER
        set(value) {
            field = value
            _binding?.countEt?.inputType = value
        }

    private var _binding: AlCountEditTextLayoutBinding? = null
    private val binding get() = _binding!!

    var count: Double = 0.0
    var max: Int = 100
    var min: Int = 0
    var infiniteCount = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    )

    override fun loadFromAttributes(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AlCountEditTextLayout)
        try {
            inputType = a.getInt(
                R.styleable.AlCountEditTextLayout_al_inputType,
                InputType.TYPE_CLASS_NUMBER
            )
            infiniteCount = a.getBoolean(
                R.styleable.AlCountEditTextLayout_al_infinite_count,
                false
            )
        } finally {
            a.recycle()
        }

        super.loadFromAttributes(attrs)
    }

    override fun initialize() {
        super.initialize()
        _binding = AlCountEditTextLayoutBinding.inflate(LayoutInflater.from(context))
        binding.countEt.inputType = inputType
        addView(binding.root)
        binding.init()
    }

    private fun AlCountEditTextLayoutBinding.init() {
        countPlusIv.setOnClickListener {
            when (inputType) {
                InputType.TYPE_NUMBER_FLAG_DECIMAL -> {
                    count += 0.5
                }
                else -> count += 1
            }

            updateView()

        }
        countMinusIv.setOnClickListener {
            when (inputType) {
                InputType.TYPE_NUMBER_FLAG_DECIMAL -> {
                    count -= 0.5
                }
                else -> count -= 1
            }

            updateView()
        }

        countEt.doOnTextChanged { text, _, _, _ ->
            val textToUpdate = text?.toString()?.trim()
            val countToUpdate = textToUpdate?.toDoubleOrNull() ?: 0.0
            if (count != countToUpdate || textToUpdate.isNullOrEmpty()) {
                count = countToUpdate
                updateView()
            }
        }
    }

    fun updateView() {
        if (count > max && !infiniteCount) {
            count = max.toDouble()
        } else if (count < min) {
            count = min.toDouble()
        }

        when (inputType) {
            InputType.TYPE_NUMBER_FLAG_DECIMAL -> {
                val countToUpdate = count.toString()
                binding.countEt.setText(countToUpdate)
                binding.countEt.setSelection(countToUpdate.length)
            }
            else -> {
                val countToUpdate = count.toInt().toString()
                binding.countEt.setText(countToUpdate)
                binding.countEt.setSelection(countToUpdate.length)
            }
        }
        onCountChangeListener?.invoke(count)
    }

    fun updateCount(count: Double?) {
        this.count = count ?: 0.0
        updateView()
    }

    fun updateCount(count: Int?) {
        this.count = count?.toDouble() ?: 0.0
        updateView()
    }

}