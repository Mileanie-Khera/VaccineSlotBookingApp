package com.study.utils

import java.math.BigInteger
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*

const val MMM_DD_YYYY_HH_MM_SS = "MMM dd,yyyy HH:mm:ss"
const val DD_MM_YYYY = "dd-MM-yyyy"
const val TARGET_FORMAT = "dd MMM yyyy"
const val TARGET_FORMAT_TIMESTAMP = "dd MMM yyyy HH:mm:ss"
fun getReadableDate(millis: Long, dateFormat: String): String? {
    val sdf = SimpleDateFormat(dateFormat)
    val date = Date(millis)
    return sdf.format(date)
}

fun isDisplayAd() = (1..100).random() % 3 == 0

fun parseStringToFormattedStringDate(
    dateString: String, dateFormat: String,
    targetDateFormat: String?
): String? {
    var formatter = SimpleDateFormat(dateFormat, Locale.US)
    return try {
        val date = formatter.parse(dateString)
        formatter = SimpleDateFormat(targetDateFormat, Locale.US)
        formatter.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

fun String.sha256(): String {
    val md = MessageDigest.getInstance("SHA-256")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}