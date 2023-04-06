package com.prashant.openapi.commonfunction

import android.widget.Toast
import androidx.annotation.StringRes
import com.prashant.openapi.activity.MainActivity
import java.text.SimpleDateFormat
import java.util.*


/**

 *This [CommonFunctions] contains common utility functions that are used across the application.
 */
object CommonFunctions {
    /**
     * This line of code initializes a private variable called  `context`.
     * It retrieves a reference to the [MainActivity] object using a weak reference.
     */
    private val context = (MainActivity.weakReference.get() as MainActivity)


    /**
    *This function returns the string resource for the given resource ID.
    *@param idRes The resource ID of the string to be retrieved.

    *@return The string value of the resource ID, or null if the context is null.
     */
    fun getStringResource(@StringRes idRes: Int) = context.getString(idRes)

    /**
     * [showToast] is an extension function on the String class that displays a toast message with the string value.
     * @param context The context in which the toast message should be displayed.
     * @return void
     */
    fun String.showToast() {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }

    /**
     * Converts UTC date string in format "yyyy-MM-dd'T'HH:mm:ss'Z'" to a normal date format of "yyyy-MM-dd HH:mm".
     * @return an empty string if the input is null.
     */
    fun convertUtcDateToNormal(utcDate: String?): String {
        if (utcDate == null) {
            return ""
        }
        val utcDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        utcDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = utcDateFormat.parse(utcDate)
        val normalDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        return normalDateFormat.format(date!!)
    }
}