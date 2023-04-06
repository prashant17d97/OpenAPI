package com.prashant.openapi.networkservice

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*


/**
 *[RetrofitApi] is an interface that defines the API endpoints for a Retrofit client. This interface
 *contains methods that define the HTTP methods and endpoint URLs for making API requests.*/
interface RetrofitApi {

    @GET(Search_Endpoint)
    suspend fun search(
        @Query("q") query: String,
        @Header("Authorization") token:String
    ): Response<JsonElement>

}