package com.aigestudio.wheelpicker.widgets

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import com.aigestudio.wheelpicker.WheelPicker
import com.aigestudio.wheelpicker.model.City
import com.aigestudio.wheelpicker.model.Province
import java.io.ObjectInputStream

/**
 * WheelAreaPicker
 * Created by Administrator on 2016/9/14 0014.
 */
class WheelAreaPicker(context: Context, attrs: AttributeSet?) : LinearLayout(context, attrs),
    IWheelAreaPicker {

    companion object {
        private const val ITEM_TEXT_SIZE = 18f
        private const val SELECTED_ITEM_COLOR = "#353535"
        private const val PROVINCE_INITIAL_INDEX = 0
    }

    private var provinceList: List<Province> = ArrayList()
    private var cityList: List<City> = ArrayList()
    private val provinceNames = ArrayList<String>()
    private val cityNames = ArrayList<String>()

    private lateinit var pickerLayoutParams: LayoutParams

    private lateinit var provincePicker: WheelPicker
    private lateinit var cityPicker: WheelPicker
    private lateinit var areaPicker: WheelPicker

    init {
        initLayoutParams()
        initView(context)
        provinceList = getJsonDataFromAssets(context) ?: ArrayList()
        obtainProvinceData()
        addListenerToWheelPicker()
    }

    @Suppress("UNCHECKED_CAST")
    private fun getJsonDataFromAssets(context: Context): List<Province>? {
        var provinceList: List<Province>? = null
        try {
            val inputStream = context.assets.open("RegionJsonData.dat")
            val objectInputStream = ObjectInputStream(inputStream)
            provinceList = objectInputStream.readObject() as? List<Province>
            objectInputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return provinceList
    }

    private fun initLayoutParams() {
        pickerLayoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        pickerLayoutParams.setMargins(5, 5, 5, 5)
        pickerLayoutParams.width = 0
    }

    private fun initView(context: Context) {
        orientation = HORIZONTAL

        provincePicker = WheelPicker(context)
        cityPicker = WheelPicker(context)
        areaPicker = WheelPicker(context)

        initWheelPicker(provincePicker, 1f)
        initWheelPicker(cityPicker, 1.5f)
        initWheelPicker(areaPicker, 1.5f)
    }

    private fun initWheelPicker(wheelPicker: WheelPicker, weight: Float) {
        pickerLayoutParams.weight = weight
        wheelPicker.itemTextSize = dip2px(context, ITEM_TEXT_SIZE)
        wheelPicker.selectedItemTextColor = Color.parseColor(SELECTED_ITEM_COLOR)
        wheelPicker.isCurved = true
        wheelPicker.layoutParams = pickerLayoutParams
        addView(wheelPicker)
    }

    private fun obtainProvinceData() {
        if (provinceList.isEmpty()) return
        for (province in provinceList) {
            province.name?.let { provinceNames.add(it) }
        }
        provincePicker.data = provinceNames
        setCityAndAreaData(PROVINCE_INITIAL_INDEX)
    }

    private fun addListenerToWheelPicker() {
        //监听省份的滑轮,根据省份的滑轮滑动的数据来设置市跟地区的滑轮数据
        provincePicker.setOnItemSelectedListener(object : WheelPicker.OnItemSelectedListener {
            override fun onItemSelected(picker: WheelPicker, data: Any?, position: Int) {
                //获得该省所有城市的集合
                if (position < provinceList.size) {
                    cityList = provinceList[position].city
                    setCityAndAreaData(position)
                }
            }
        })

        cityPicker.setOnItemSelectedListener(object : WheelPicker.OnItemSelectedListener {
            override fun onItemSelected(picker: WheelPicker, data: Any?, position: Int) {
                //获取城市对应的城区的名字
                if (position < cityList.size) {
                    areaPicker.data = cityList[position].area
                    areaPicker.selectedItemPosition = 0
                }
            }
        })
    }

    private fun setCityAndAreaData(position: Int) {
        if (position >= provinceList.size) return
        //获得该省所有城市的集合
        cityList = provinceList[position].city
        //获取所有city的名字
        //重置先前的城市集合数据
        cityNames.clear()
        for (city in cityList)
            city.name?.let { cityNames.add(it) }
        cityPicker.data = cityNames
        cityPicker.selectedItemPosition = 0
        //获取第一个城市对应的城区的名字
        //重置先前的城区集合的数据
        if (cityList.isNotEmpty()) {
            areaPicker.data = cityList[0].area
            areaPicker.selectedItemPosition = 0
        } else {
            areaPicker.data = emptyList<String>()
        }
    }

    override val province: String?
        get() = provinceList.getOrNull(provincePicker.currentItemPosition)?.name

    override val city: String?
        get() = cityList.getOrNull(cityPicker.currentItemPosition)?.name

    override val area: String?
        get() = cityList.getOrNull(cityPicker.currentItemPosition)?.area?.getOrNull(areaPicker.currentItemPosition)

    override fun hideArea() {
        if (indexOfChild(areaPicker) != -1) {
            removeView(areaPicker)
        }
    }

    private fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }
}