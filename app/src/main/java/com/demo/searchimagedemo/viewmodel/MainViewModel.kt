package com.demo.searchimagedemo.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.demo.searchimagedemo.model.data.Photo
import com.demo.searchimagedemo.model.response.ImageSearchResponse
import com.demo.searchimagedemo.repository.Fail
import com.demo.searchimagedemo.repository.InternetNotConnected
import com.demo.searchimagedemo.repository.Repository
import com.demo.searchimagedemo.repository.Success
import com.demo.searchimagedemo.utils.Helper
import com.demo.searchimagedemo.utils.Logger
import com.demo.searchimagedemo.utils.SingleLiveEvent
import com.demo.searchimagedemo.repository.Result
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class MainViewModel @Inject constructor(private val mRepository: Repository) : ViewModel() {

    var mImageResponseLiveData = SingleLiveEvent<Result<*>>()
        private set
    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var mDataRxObserver: Observer<ImageSearchResponse>

    // configuration handling
    var mImageData: ArrayList<Photo> = ArrayList()
    var mSearchTxt: String? = null
    var mNextPage: Int = 1
    var mTotalCount: Int = 0

    companion object {
        private val LOG_TAG = MainViewModel::class.java.simpleName
    }

    fun getImagesByText(
        context: Context,
        pPageNo: String,
        pSearchText: String,
        pPageSize: String
    ): SingleLiveEvent<Result<*>> {
        if (Helper.isInternetOn(context)) {
            // observer init
            initRxResponseObserver()
            // api call
            mRepository.getImagesByText(pSearchText, pPageSize, pPageNo).subscribe(mDataRxObserver)
            return mImageResponseLiveData
        } else {
            mImageResponseLiveData.value = InternetNotConnected
            return mImageResponseLiveData
        }
    }


    private fun initRxResponseObserver() {
        if (!this::mDataRxObserver.isInitialized) {
            mDataRxObserver = object : Observer<ImageSearchResponse> {
                override fun onComplete() {
                    Logger.debugLog(LOG_TAG, "Response($this)-onComplete()")
                }

                override fun onSubscribe(d: Disposable) {
                    Logger.debugLog(LOG_TAG, "Response($this)-onSubscribe()")
                    mCompositeDisposable.add(d)
                }

                override fun onNext(response: ImageSearchResponse) {
                    Logger.debugLog(
                        LOG_TAG,
                        "Response($this--${response.photos.photo.size})-onNext()"
                    )
                    if (response.photos.photo.isNullOrEmpty().not()) {
                        mImageResponseLiveData.value = Success(response)
                        Logger.debugLog(
                            LOG_TAG,
                            "Response(${response.photos.photo[0].getImgUrl()})"
                        )
                    } else {
                        mImageResponseLiveData.value = Fail(Exception("something went wrong"))
                    }
                }

                override fun onError(e: Throwable) {
                    Logger.debugLog(LOG_TAG, "Response($this--${e.message})-onError()")
                    mImageResponseLiveData.value = Fail(e)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Logger.debugLog(LOG_TAG, "- onCleared()" + "-$this")
        mCompositeDisposable.dispose()
    }

}
