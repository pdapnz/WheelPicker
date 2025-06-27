package com.aigestudio.wheelpicker.demo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aigestudio.wheelpicker.WheelPicker
import com.aigestudio.wheelpicker.demo.databinding.AcPreviewBinding

/**
 * @author AigeStudio 2015-12-06
 * @author AigeStudio 2016-07-08
 */
class PreviewActivity : AppCompatActivity(), WheelPicker.OnItemSelectedListener {
    private lateinit var binding: AcPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AcPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainWheelLeft.setOnItemSelectedListener(this)
        binding.mainWheelCenter.setOnItemSelectedListener(this)
        binding.mainWheelRight.setOnItemSelectedListener(this)
    }

    override fun onItemSelected(picker: WheelPicker, data: Any?, position: Int) {
        val text = when (picker.id) {
            binding.mainWheelLeft.id -> "Left:"
            binding.mainWheelCenter.id -> "Center:"
            binding.mainWheelRight.id -> "Right:"
            else -> ""
        }
        Toast.makeText(this, text + data.toString(), Toast.LENGTH_SHORT).show()
    }
}