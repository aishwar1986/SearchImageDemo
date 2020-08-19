package com.demo.searchimagedemo.repository

import com.demo.searchimagedemo.model.response.ImageSearchResponse
import com.demo.searchimagedemo.repository.service.RemoteService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class Repository @Inject constructor(private val remoteService: RemoteService) {

    fun getImagesByText(
        pSearchTxt: String,
        pPageSize: String,
        pPageNo: String
    ): Observable<ImageSearchResponse> {
        return remoteService.getImageListForText(
            pSearchText = pSearchTxt,
            pPageNo = pPageNo,
            pPageSize = pPageSize
        ).subscribeOn(
            Schedulers.io()
        ).observeOn(AndroidSchedulers.mainThread())
    }
}