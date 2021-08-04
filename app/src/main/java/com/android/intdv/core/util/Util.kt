package com.android.intdv.core.util

import android.icu.text.DateFormat
import android.os.Build
import java.text.SimpleDateFormat
import java.util.*

object Util {

    /**
     * Returns the formatted date to display
     * @param dateStr - date string as input to format
     *
     * @return - formatted date string
     */
    fun getFormattedDate(dateStr : String) : String {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val df: DateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH)
                df.format(formatter.parse(dateStr))
            } else {
                dateStr
            }

        } catch (e : Exception) {
            e.printStackTrace()
            dateStr
        }
    }

    /**
     * Returns the URL for the given image path
     * @param imagePath - path of the image
     *
     * @return image url
     */
    fun getOriginalImageUrl(imagePath : String) : String {
        return "https://image.tmdb.org/t/p/original/${imagePath}"
    }
}