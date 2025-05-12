package com.aigestudio.wheelpicker.widgets

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.aigestudio.wheelpicker.IDebug
import com.aigestudio.wheelpicker.IWheelPicker
import com.aigestudio.wheelpicker.R
import com.aigestudio.wheelpicker.WheelPicker
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class WheelDatePicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs), WheelPicker.OnItemSelectedListener, IDebug, IWheelPicker,
    IWheelDatePicker, IWheelYearPicker, IWheelMonthPicker, IWheelDayPicker {

    companion object {
        private val SDF = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
    }

    private val pickerYear: WheelYearPicker
    private val pickerMonth: WheelMonthPicker
    private val pickerDay: WheelDayPicker

    private var onDateSelectedListener: OnDateSelectedListener? = null

    private val tvYear: TextView
    private val tvMonth: TextView
    private val tvDay: TextView

    private var _year: Int = 0
    private var _month: Int = 0
    private var _day: Int = 0

    override val wheelYearPicker: WheelYearPicker
        get() = pickerYear

    override val wheelMonthPicker: WheelMonthPicker
        get() = pickerMonth

    override val wheelDayPicker: WheelDayPicker
        get() = pickerDay

    override val textViewYear: TextView
        get() = tvYear

    override val textViewMonth: TextView
        get() = tvMonth

    override val textViewDay: TextView
        get() = tvDay

    init {
        LayoutInflater.from(context).inflate(R.layout.view_wheel_date_picker, this)

        pickerYear = findViewById(R.id.wheel_date_picker_year)
        pickerMonth = findViewById(R.id.wheel_date_picker_month)
        pickerDay = findViewById(R.id.wheel_date_picker_day)
        pickerYear.setOnItemSelectedListener(this)
        pickerMonth.setOnItemSelectedListener(this)
        pickerDay.setOnItemSelectedListener(this)

        setMaximumWidthTextYear()
        pickerMonth.maximumWidthText = "00"
        pickerDay.maximumWidthText = "00"

        tvYear = findViewById(R.id.wheel_date_picker_year_tv)
        tvMonth = findViewById(R.id.wheel_date_picker_month_tv)
        tvDay = findViewById(R.id.wheel_date_picker_day_tv)

        _year = pickerYear.currentYear
        _month = pickerMonth.currentMonth
        _day = pickerDay.currentDay
    }

    private fun setMaximumWidthTextYear() {
        val years = pickerYear.data ?: return
        if (years.isEmpty()) return
        val lastYear = years.last().toString()
        val sb = StringBuilder()
        for (i in lastYear.indices) sb.append("0")
        pickerYear.maximumWidthText = sb.toString()
    }

    override fun onItemSelected(picker: WheelPicker, data: Any?, position: Int) {
        if (picker.id == R.id.wheel_date_picker_year) {
            _year = data.toString().toInt()
            pickerDay.year = _year
        } else if (picker.id == R.id.wheel_date_picker_month) {
            _month = data.toString().toInt()
            pickerDay.month = _month
        }
        _day = pickerDay.currentDay
        val date = "$_year-$_month-$_day"
        try {
            val parsedDate = SDF.parse(date)
            if (parsedDate != null) {
                onDateSelectedListener?.onDateSelected(this, parsedDate)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    override fun setDebug(isDebug: Boolean) {
        pickerYear.setDebug(isDebug)
        pickerMonth.setDebug(isDebug)
        pickerDay.setDebug(isDebug)
    }

    override var visibleItemCount: Int
        get() {
            if (pickerYear.visibleItemCount == pickerMonth.visibleItemCount &&
                pickerMonth.visibleItemCount == pickerDay.visibleItemCount
            ) {
                return pickerYear.visibleItemCount
            }
            throw ArithmeticException("Can not get visible item count correctly from WheelDatePicker!")
        }
        set(count) {
            pickerYear.visibleItemCount = count
            pickerMonth.visibleItemCount = count
            pickerDay.visibleItemCount = count
        }

    override var isCyclic: Boolean
        get() = pickerYear.isCyclic && pickerMonth.isCyclic && pickerDay.isCyclic
        set(isCyclic) {
            pickerYear.isCyclic = isCyclic
            pickerMonth.isCyclic = isCyclic
            pickerDay.isCyclic = isCyclic
        }

    @Deprecated("Unsupported", ReplaceWith("nothing"))
    override fun setOnItemSelectedListener(listener: WheelPicker.OnItemSelectedListener?) {
        throw UnsupportedOperationException("You can not set OnItemSelectedListener for WheelDatePicker")
    }

    @Deprecated("Unsupported", ReplaceWith("nothing"))
    override var selectedItemPosition: Int
        get() = throw UnsupportedOperationException("You can not get position of selected item from WheelDatePicker")
        set(_) {
            throw UnsupportedOperationException("You can not set position of selected item for WheelDatePicker")
        }

    @Deprecated("Unsupported", ReplaceWith("nothing"))
    override val currentItemPosition: Int
        get() = throw UnsupportedOperationException("You can not get position of current item from WheelDatePicker")

    @Deprecated("Unsupported", ReplaceWith("nothing"))
    override var data: List<*>?
        get() = throw UnsupportedOperationException("You can not get data source from WheelDatePicker")
        set(_) {
            throw UnsupportedOperationException("You don't need to set data source for WheelDatePicker")
        }


    @Deprecated("Unsupported", ReplaceWith("nothing"))
    override fun setSameWidth(hasSameSize: Boolean) {
        throw UnsupportedOperationException("You don't need to set same width for WheelDatePicker")
    }

    @Deprecated("Unsupported", ReplaceWith("nothing"))
    override fun hasSameWidth(): Boolean {
        throw UnsupportedOperationException("You don't need to set same width for WheelDatePicker")
    }

    @Deprecated("Unsupported", ReplaceWith("nothing"))
    override fun setOnWheelChangeListener(listener: WheelPicker.OnWheelChangeListener?) {
        throw UnsupportedOperationException("WheelDatePicker unsupport set OnWheelChangeListener")
    }

    @Deprecated("Unsupported", ReplaceWith("nothing"))
    override var maximumWidthText: String?
        get() = throw UnsupportedOperationException("You can not get maximum width text from WheelDatePicker")
        set(_) {
            throw UnsupportedOperationException("You don't need to set maximum width text for WheelDatePicker")
        }

    @Deprecated("Unsupported", ReplaceWith("nothing"))
    override var maximumWidthTextPosition: Int
        get() = throw UnsupportedOperationException("You can not get maximum width text position from WheelDatePicker")
        set(_) {
            throw UnsupportedOperationException("You don't need to set maximum width text position for WheelDatePicker")
        }

    override var selectedItemTextColor: Int
        get() {
            if (pickerYear.selectedItemTextColor == pickerMonth.selectedItemTextColor &&
                pickerMonth.selectedItemTextColor == pickerDay.selectedItemTextColor
            ) {
                return pickerYear.selectedItemTextColor
            }
            throw RuntimeException("Can not get color of selected item text correctly from WheelDatePicker!")
        }
        set(color) {
            pickerYear.selectedItemTextColor = color
            pickerMonth.selectedItemTextColor = color
            pickerDay.selectedItemTextColor = color
        }

    override var itemTextColor: Int
        get() {
            if (pickerYear.itemTextColor == pickerMonth.itemTextColor &&
                pickerMonth.itemTextColor == pickerDay.itemTextColor
            ) {
                return pickerYear.itemTextColor
            }
            throw RuntimeException("Can not get color of item text correctly from WheelDatePicker!")
        }
        set(color) {
            pickerYear.itemTextColor = color
            pickerMonth.itemTextColor = color
            pickerDay.itemTextColor = color
        }

    override var itemTextSize: Int
        get() {
            if (pickerYear.itemTextSize == pickerMonth.itemTextSize &&
                pickerMonth.itemTextSize == pickerDay.itemTextSize
            ) {
                return pickerYear.itemTextSize
            }
            throw RuntimeException("Can not get size of item text correctly from WheelDatePicker!")
        }
        set(size) {
            pickerYear.itemTextSize = size
            pickerMonth.itemTextSize = size
            pickerDay.itemTextSize = size
        }

    override var itemSpace: Int
        get() {
            if (pickerYear.itemSpace == pickerMonth.itemSpace &&
                pickerMonth.itemSpace == pickerDay.itemSpace
            ) {
                return pickerYear.itemSpace
            }
            throw RuntimeException("Can not get item space correctly from WheelDatePicker!")
        }
        set(space) {
            pickerYear.itemSpace = space
            pickerMonth.itemSpace = space
            pickerDay.itemSpace = space
        }

    override var isIndicator: Boolean
        get() = pickerYear.isIndicator && pickerMonth.isIndicator && pickerDay.isIndicator
        set(hasIndicator) {
            pickerYear.isIndicator = hasIndicator
            pickerMonth.isIndicator = hasIndicator
            pickerDay.isIndicator = hasIndicator
        }

    override var indicatorSize: Int
        get() {
            if (pickerYear.indicatorSize == pickerMonth.indicatorSize &&
                pickerMonth.indicatorSize == pickerDay.indicatorSize
            ) {
                return pickerYear.indicatorSize
            }
            throw RuntimeException("Can not get indicator size correctly from WheelDatePicker!")
        }
        set(size) {
            pickerYear.indicatorSize = size
            pickerMonth.indicatorSize = size
            pickerDay.indicatorSize = size
        }

    override var indicatorColor: Int
        get() {
            if (pickerYear.indicatorColor == pickerMonth.indicatorColor &&
                pickerMonth.indicatorColor == pickerDay.indicatorColor
            ) {
                return pickerYear.indicatorColor
            }
            throw RuntimeException("Can not get indicator color correctly from WheelDatePicker!")
        }
        set(color) {
            pickerYear.indicatorColor = color
            pickerMonth.indicatorColor = color
            pickerDay.indicatorColor = color
        }

    override var isCurtain: Boolean
        get() = pickerYear.isCurtain && pickerMonth.isCurtain && pickerDay.isCurtain
        set(hasCurtain) {
            pickerYear.isCurtain = hasCurtain
            pickerMonth.isCurtain = hasCurtain
            pickerDay.isCurtain = hasCurtain
        }

    override var curtainColor: Int
        get() {
            if (pickerYear.curtainColor == pickerMonth.curtainColor &&
                pickerMonth.curtainColor == pickerDay.curtainColor
            ) {
                return pickerYear.curtainColor
            }
            throw RuntimeException("Can not get curtain color correctly from WheelDatePicker!")
        }
        set(color) {
            pickerYear.curtainColor = color
            pickerMonth.curtainColor = color
            pickerDay.curtainColor = color
        }

    override var isAtmospheric: Boolean
        get() = pickerYear.isAtmospheric && pickerMonth.isAtmospheric && pickerDay.isAtmospheric
        set(hasAtmospheric) {
            pickerYear.isAtmospheric = hasAtmospheric
            pickerMonth.isAtmospheric = hasAtmospheric
            pickerDay.isAtmospheric = hasAtmospheric
        }

    override var isCurved: Boolean
        get() = pickerYear.isCurved && pickerMonth.isCurved && pickerDay.isCurved
        set(isCurved) {
            pickerYear.isCurved = isCurved
            pickerMonth.isCurved = isCurved
            pickerDay.isCurved = isCurved
        }

    @Deprecated("Unsupported", ReplaceWith("nothing"))
    override var itemAlign: Int
        get() = throw UnsupportedOperationException("You can not get item align from WheelDatePicker")
        set(_) {
            throw UnsupportedOperationException("You don't need to set item align for WheelDatePicker")
        }

    override var typeface: Typeface?
        get() {
            if (pickerYear.typeface == pickerMonth.typeface &&
                pickerMonth.typeface == pickerDay.typeface
            ) {
                return pickerYear.typeface
            }
            throw RuntimeException("Can not get typeface correctly from WheelDatePicker!")
        }
        set(tf) {
            pickerYear.typeface = tf
            pickerMonth.typeface = tf
            pickerDay.typeface = tf
        }

    override fun setOnDateSelectedListener(listener: OnDateSelectedListener?) {
        onDateSelectedListener = listener
    }

    override val currentDate: Date
        get() {
            val date = "$_year-$_month-$_day"
            return try {
                SDF.parse(date) ?: Date()
            } catch (e: ParseException) {
                e.printStackTrace()
                Date()
            }
        }

    override var itemAlignYear: Int
        get() = pickerYear.itemAlign
        set(align) {
            pickerYear.itemAlign = align
        }

    override var itemAlignMonth: Int
        get() = pickerMonth.itemAlign
        set(align) {
            pickerMonth.itemAlign = align
        }

    override var itemAlignDay: Int
        get() = pickerDay.itemAlign
        set(align) {
            pickerDay.itemAlign = align
        }

    override fun setYearFrame(start: Int, end: Int) {
        pickerYear.setYearFrame(start, end)
    }

    override var yearStart: Int
        get() = pickerYear.yearStart
        set(start) {
            pickerYear.yearStart = start
        }

    override var yearEnd: Int
        get() = pickerYear.yearEnd
        set(end) {
            pickerYear.yearEnd = end
        }

    override var selectedYear: Int
        get() = pickerYear.selectedYear
        set(year) {
            _year = year
            pickerYear.selectedYear = year
            pickerDay.year = year
        }

    override val currentYear: Int
        get() = pickerYear.currentYear

    override var selectedMonth: Int
        get() = pickerMonth.selectedMonth
        set(month) {
            _month = month
            pickerMonth.selectedMonth = month
            pickerDay.month = month
        }

    override val currentMonth: Int
        get() = pickerMonth.currentMonth

    override var selectedDay: Int
        get() = pickerDay.selectedDay
        set(day) {
            _day = day
            pickerDay.selectedDay = day
        }

    override val currentDay: Int
        get() = pickerDay.currentDay

    override fun setYearAndMonth(year: Int, month: Int) {
        _year = year
        _month = month
        pickerYear.selectedYear = year
        pickerMonth.selectedMonth = month
        pickerDay.setYearAndMonth(year, month)
    }

    override var year: Int
        get() = selectedYear
        set(year) {
            _year = year
            pickerYear.selectedYear = year
            pickerDay.year = year
        }

    override var month: Int
        get() = selectedMonth
        set(month) {
            _month = month
            pickerMonth.selectedMonth = month
            pickerDay.month = month
        }

    interface OnDateSelectedListener {
        fun onDateSelected(picker: WheelDatePicker, date: Date)
    }
}