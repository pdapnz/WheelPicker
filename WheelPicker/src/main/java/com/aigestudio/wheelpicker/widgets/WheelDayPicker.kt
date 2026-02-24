package com.aigestudio.wheelpicker.widgets

import android.content.Context
import android.util.AttributeSet
import com.aigestudio.wheelpicker.WheelPicker
import java.util.*

/**
 * 日期选择器
 *
 * Picker for Day
 *
 * @author AigeStudio 2016-07-12
 * @version 1
 */
class WheelDayPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : WheelPicker(context, attrs), IWheelDayPicker {

    companion object {
        private val DAYS: MutableMap<Int, List<Int>> = HashMap()
    }

    private val calendar = Calendar.getInstance()

    private var _year = calendar.get(Calendar.YEAR)
    private var _month = calendar.get(Calendar.MONTH)
    private var _selectedDay = calendar.get(Calendar.DAY_OF_MONTH)

    override var selectedDay: Int
        get() = _selectedDay
        set(value) {
            _selectedDay = value
            updateSelectedDay()
        }

    override val currentDay: Int
        get() = Integer.valueOf(data!![currentItemPosition].toString())

    override var year: Int
        get() = _year
        set(value) {
            _year = value
            updateDays()
        }

    override var month: Int
        get() = _month
        set(value) {
            _month = value - 1
            updateDays()
        }

    init {
        updateDays()
        updateSelectedDay()
    }

    private fun updateDays() {
        calendar.set(Calendar.YEAR, _year)
        calendar.set(Calendar.MONTH, _month)

        val days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        var dataList = DAYS[days]
        if (null == dataList) {
            dataList = ArrayList()
            for (i in 1..days) {
                dataList.add(i)
            }
            DAYS[days] = dataList
        }
        super.data = dataList
    }

    private fun updateSelectedDay() {
        selectedItemPosition = _selectedDay - 1
    }

    override fun setYearAndMonth(year: Int, month: Int) {
        _year = year
        _month = month - 1
        updateDays()
    }
}
