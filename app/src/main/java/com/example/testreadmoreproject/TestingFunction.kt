package com.example.testreadmoreproject

import android.util.Log
import java.text.DecimalFormat
import java.text.NumberFormat
import kotlin.math.pow

object TestingFunction {
    fun formatValueToMatrixUnit(paramValue: Double): String? {
        if (paramValue == 0.0) {
            return "0"
        }
        var value = paramValue
        val power: Int
        val suffix = " KM"
        val formatter: NumberFormat = DecimalFormat("#,###.#")
        power = StrictMath.log10(value).toInt()
        value /= 10.0.pow(power * 3 / 3.toDouble())
        var formattedNumber = formatter.format(value)
        formattedNumber += suffix[power / 3]
        return if (formattedNumber.length > 4) formattedNumber.replace(
            "\\.[0-9]+".toRegex(),
            ""
        ) else formattedNumber
    }

    fun remove(param: String): String {
        var testing = ""
        if (param.length > 4) {
            testing = param.replace(
                "\\.[0-9]+".toRegex(),
                ""
            ) //Remove decimal precision if the result is more than 3 digit number
        }
        return testing
    }
}