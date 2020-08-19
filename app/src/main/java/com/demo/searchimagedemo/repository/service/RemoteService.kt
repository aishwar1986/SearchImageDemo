package com.demo.searchimagedemo.repository.service

import com.demo.searchimagedemo.model.response.ImageSearchResponse
import com.demo.searchimagedemo.utils.Constants
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteService {
    @GET("rest/")
    fun getImageListForText(
        @Query("method") pMethodName: String = Constants.API_URL_SEARCH_IMG_METHOD_NAME,
        @Query("text") pSearchText: String,
        @Query("per_page") pPageSize: String,
        @Query("page") pPageNo: String
    ):Observable<ImageSearchResponse>
}