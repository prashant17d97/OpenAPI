package com.prashant.openapi.networkservice

import com.google.gson.JsonElement
import retrofit2.Response

interface ApiProcessor {
    suspend fun sendRequest(retrofitApi: RetrofitApi): Response<JsonElement>
}