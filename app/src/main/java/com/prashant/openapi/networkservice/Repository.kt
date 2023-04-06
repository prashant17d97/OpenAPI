package com.prashant.openapi.networkservice

import android.util.Log
import com.prashant.openapi.R
import com.prashant.openapi.activity.MainActivity
import com.prashant.openapi.commonfunction.CommonFunctions.getStringResource
import com.prashant.openapi.commonfunction.CommonFunctions.showToast
import com.prashant.openapi.networkservice.NetworkExtensionFunction.hideProgress
import com.prashant.openapi.networkservice.NetworkExtensionFunction.isNetworkAvailable
import com.prashant.openapi.networkservice.NetworkExtensionFunction.jsonToData
import com.prashant.openapi.networkservice.NetworkExtensionFunction.showProgress
import com.prashant.openapi.screens.search.model.GithubResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

/**

*[Repository] class is responsible for handling all the API calls. It accepts RetrofitApi as a parameter through constructor injection.
*@param retrofitApi an instance of RetrofitApi.
 */
class Repository @Inject constructor(
    val retrofitApi: RetrofitApi
) {

    /**

    *This function performs the API call asynchronously and returns the result as a generic type.

    *@param retrofitCall an instance of ApiProcessor which holds the API call request.

    *@param loader a boolean value to show the progress bar during the API call.

    *@param result a lambda function that takes the API response data as a parameter and performs the required operation.

    *@param responseMessage a lambda function that takes the response message and response code as parameters and performs the required operation.
     */
    suspend inline fun <reified Generic> apiCall(
        retrofitCall: ApiProcessor,
        loader: Boolean = false,
        crossinline result: (result: Generic) -> Unit,
        crossinline responseMessage: (message: String, code: Int) -> Unit
    ) {
        try {
            if (!(MainActivity.weakReference.get() as MainActivity).isNetworkAvailable()) {
                getStringResource(R.string.your_device_offline).showToast()
                return
            } else {
                if (loader)
                    showProgress()
                val response = flow {
                    emit(retrofitCall.sendRequest(retrofitApi))
                }.flowOn(Dispatchers.IO)
                response.catch { exception ->
                    exception.printStackTrace()
                    hideProgress()
                    (exception.message ?: "").showToast()
                }.collect {
                    hideProgress()
                    when (it.code()) {
                        /**Some error occurred*/
                        in 100..199 -> {
                            responseMessage(it.message(), it.code())
                            Log.e(
                                TAG,
                                "OpenAPI:---> Message:${it.message()} ResponseCode: ${it.code()}"
                            )
                        }

                        /**
                         * Successful
                         * */
                        200 -> {
                            Log.e(
                                TAG,
                                "OpenAPI:---> Message:${it.message()} ResponseCode: ${it.code()}"
                            )
                            if (it.isSuccessful && it.body()?.isJsonNull == false) {
                                jsonToData<Generic>(it.body()) { resultData ->
                                    result(resultData)
                                }
                            }
                        }
                        in 300..399 -> {
                            responseMessage(it.message(), it.code())
                            getStringResource(R.string.someError).showToast()
                            Log.e(
                                TAG,
                                "OpenAPI:---> Message:${it.message()} ResponseCode: ${it.code()}"
                            )
                        }

                        /**Unauthorized*/
                        401 -> {
                            responseMessage(it.message(), it.code())
                            Log.e(
                                TAG,
                                "OpenAPI:---> Message:${it.message()} ResponseCode: ${it.code()}"
                            )
                        }

                        /**Page not found*/
                        404 -> {
                            responseMessage(it.message(), it.code())
                            Log.e(
                                TAG,
                                "OpenAPI:---> Message:${it.message()} ResponseCode: ${it.code()}"
                            )
                        }

                        /**Server Error*/
                        in 501..509 -> {
                            responseMessage(it.message(), it.code())
                            Log.e(
                                TAG,
                                "OpenAPI:---> Message:${it.message()} ResponseCode: ${it.code()}"
                            )
                        }

                        else -> {
                            /**ClientErrors*/
                            val res = it.errorBody()!!.string()
                            val jsonObject = JSONObject(res)
                            when {
                                jsonObject.has("message") -> {
                                    responseMessage(jsonObject.getString("message"), it.code())
                                    Log.e(
                                        TAG,
                                        "OpenAPI:---> Message:${jsonObject.getString("message")} ResponseCode: ${it.code()}"
                                    )
                                }
                                jsonObject.has("error") -> {
                                    val message =
                                        jsonObject.getJSONObject("error").getString("text") ?: ""
                                    responseMessage(message, it.code())
                                    Log.e(
                                        TAG,
                                        "OpenAPI:---> Message:${message} ResponseCode: ${it.code()}"
                                    )

                                    getStringResource(R.string.someError).showToast()
                                }
                                else -> {
                                    getStringResource(R.string.someError).showToast()
                                }
                            }
                        }
                    }
                }
            }
        } catch (exception: Exception) {
            Log.e(TAG, "call: ${exception.message}")
        }

    }

    companion object {
        const val TAG = "Repository"
    }
}