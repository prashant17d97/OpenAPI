package com.prashant.openapi.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonElement
import com.prashant.openapi.R
import com.prashant.openapi.commonfunction.CommonFunctions.getStringResource
import com.prashant.openapi.commonfunction.CommonFunctions.showToast
import com.prashant.openapi.networkservice.ApiProcessor
import com.prashant.openapi.networkservice.Repository
import com.prashant.openapi.networkservice.RetrofitApi
import com.prashant.openapi.screens.search.model.GithubItem
import com.prashant.openapi.screens.search.model.GithubResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

@HiltViewModel
class SearchVM @Inject constructor(
    private val retrofit: Retrofit,
    private val repository: Repository
) : ViewModel() {

    var items by mutableStateOf(listOf<GithubItem?>())
    private val token =
        "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"

    fun query(query: String) {
        viewModelScope.launch {
            repository.apiCall<GithubResponse>(
                retrofitCall = object : ApiProcessor {
                    override suspend fun sendRequest(retrofitApi: RetrofitApi): Response<JsonElement> {
                        return retrofitApi.search(query = query, token = token)
                    }
                },
                loader = true,
                result = { resultResponse ->
                    Log.e(TAG, "query: ${resultResponse.items?.size}")
                    resultResponse.items?.let { itemList ->
                        items = itemList
                    }
                    if ((resultResponse.items?.size ?: 0) > 0) {
                        "${resultResponse.items?.size ?: 0} items has fetched.".showToast()
                    }
                },
                responseMessage = { message: String, code: Int ->
                    Log.e(TAG, "OpenAPI: $message $code")
                    message.takeIf { it.isNotEmpty() }
                        ?: "$code ${getStringResource(R.string.someError)}".showToast()
                })
        }
    }

    companion object {
        private const val TAG = "SearchVM"
    }
}