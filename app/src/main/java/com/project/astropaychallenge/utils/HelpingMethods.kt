package com.project.astropaychallenge.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

fun parseKelvinToCelsius(number: Float): String {
        val decimalFormat = DecimalFormatSymbols()
        decimalFormat.decimalSeparator = '.'
        val formatNumber = DecimalFormat("#", decimalFormat)
        val celsiusTemp = formatNumber.format(number - 273.15)
        return "$celsiusTemp °C"
}