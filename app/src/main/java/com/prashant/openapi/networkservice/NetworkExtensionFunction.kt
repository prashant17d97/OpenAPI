package com.prashant.openapi.networkservice

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.prashant.openapi.activity.MainActivity

object NetworkExtensionFunction {

    /**
     *Converts a [JsonElement] to a data class of generic type [Generic] using Gson library.
     *The resulting data class is passed to the [response] lambda function as a parameter.
     *@param responseData The [JsonElement] to be converted to [Generic] data class.
     *@param response The lambda function that takes the resulting [Generic] data class as a parameter.
     */
    inline fun <reified Generic> jsonToData(
        responseData: JsonElement?,
        response: (Generic) -> Unit
    ) {
        if (responseData != null) {
            val typeToken = object : TypeToken<Generic>() {}.type
            response(Gson().fromJson(responseData, typeToken))
        }
    }

    /**
     *This function is used to show a progress loader in the main activity.
     * It obtains a weak reference to the main activity and calls the "showLoader" method on it with a boolean value of "true".
     */
    fun showProgress() {
        (MainActivity.weakReference.get() as MainActivity).showLoader(boolean = true)
    }

    /**
     *Hides the progress loader in MainActivity.
     *If MainActivity is not available or its weak reference is null,
     *this method does nothing.
     */
    fun hideProgress() {
        (MainActivity.weakReference.get() as MainActivity).showLoader(boolean = false)
    }


    /**
     *This function checks if the device is currently connected to a network, whether it be wifi, cellular data, or ethernet.
     *It uses the [ConnectivityManager] service to check the network capabilities of the device's active network.
     *@return true if the device is connected to a network, false otherwise.
     */
    fun Context.isNetworkAvailable(): Boolean {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.getNetworkCapabilities(
            connectivityManager.activeNetwork ?: return false
        ) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}