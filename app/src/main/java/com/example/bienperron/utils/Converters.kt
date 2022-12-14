package com.example.bienperron.utils

import java.text.DecimalFormat

fun Number?.toCurrencyString(): String {
    val format = DecimalFormat("\$ #,##0")
    return try {
        format.format(this ?: 0)
    } catch (e: IllegalArgumentException) {
        format.format(0)
    }
}