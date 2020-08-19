package com.demo.searchimagedemo.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.searchimagedemo.application.ImageApp
import com.demo.searchimagedemo.R
import com.demo.searchimagedemo.adapter.ImageListAdapter
import com.demo.searchimagedemo.utils.Logger
import com.demo.searchimagedemo.viewmodel.MainViewModel
import com.demo.searchimagedemo.di.AppComponent
import com.demo.searchimagedemo.di.AppViewModelFactory
import com.demo.searchimagedemo.di.MainActivityComponent
import com.demo.searchimagedemo.model.data.Photo
import com.demo.searchimagedemo.model.response.ImageSearchResponse
import com.demo.searchimagedemo.repository.Fail
import com.demo.searchimagedemo.repository.InternetNotConnected
import com.demo.searchimagedemo.repository.Result
import com.demo.searchimagedemo.repository.Success
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    //DI
    @Inject
    lateinit var mViewModelFactory: AppViewModelFactory
    private lateinit var mAppComponent: AppComponent
    lateinit var mMainActivityComponent: MainActivityComponent

    private val ITEM_PER_PAGE = 20
    private lateinit var mViewModel: MainViewModel
    private lateinit var mImageDataObserver: Observer<Result<*>>

    // adapter
    private lateinit var mImageAdapter: ImageListAdapter


    companion object {
        val LOG_TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //DI
        mAppComponent = (application as ImageApp).appComponent
        mMainActivityComponent = mAppComponent.mainActivityComponent().create()
        mMainActivityComponent.inject(this)
        val provider = ViewModelProvider(this, mViewModelFactory)
        mViewModel = provider.get(MainViewModel::class.java)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(tb)
        supportActionBar?.setDisplayShowTitleEnabled(false);
        initRecyclerView()
        initObserver()
        // case : configuration change
        if (mViewModel.mImageData.isEmpty().not()) {
            mImageAdapter =
                ImageListAdapter(
                    mViewModel.mImageData,
                    mViewModel.mTotalCount
                )
            rv.adapter = mImageAdapter
            pb.visibility = View.INVISIBLE
            txt_info.visibility = View.INVISIBLE
            rv.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val item: MenuItem = menu!!.findItem(R.id.menu_search)
        val searchView: SearchView = item.getActionView() as SearchView
        searchView.setOnQueryTextListener(this)
        searchView.queryHint = "Search For Images"
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (mViewModel.mSearchTxt == query) {
            // Ignore
            return false
        }
        mViewModel.mSearchTxt = query
        mViewModel.mNextPage = 1
        mViewModel.mImageData.clear()
        pb.visibility = View.VISIBLE
        txt_info.visibility = View.INVISIBLE
        query?.let {
            mViewModel.getImagesByText(
                this,
                mViewModel.mNextPage.toString(),
                it,
                ITEM_PER_PAGE.toString()
            )
                .observe(this, mImageDataObserver)
        }
        if (this::mImageAdapter.isInitialized) {
            mImageAdapter.clearData()
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    private fun initRecyclerView() {
        // basic init
        rv.layoutManager = GridLayoutManager(this, 2)
        // pagination handling
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager?.itemCount
                val lastVisibleItem =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                Logger.debugLog(pMsg = "totalItemCount: $totalItemCount,lastVisible: $lastVisibleItem")
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (totalItemCount != null) {
                        if (mViewModel.mSearchTxt.isNullOrBlank()
                                .not() && totalItemCount < mViewModel.mTotalCount && totalItemCount <= (lastVisibleItem + 1)
                        ) {
                            mViewModel.getImagesByText(
                                this@MainActivity,
                                mViewModel.mNextPage.toString(),
                                mViewModel.mSearchTxt!!,
                                ITEM_PER_PAGE.toString()
                            )
                                .observe(this@MainActivity, mImageDataObserver)
                        }
                    }
                }
            }
        })
    }

    private fun renderUI(response: ImageSearchResponse?) {
        response?.photos?.run {
            if (!this@MainActivity::mImageAdapter.isInitialized) {
                mViewModel.mTotalCount = this.pages.toInt() * ITEM_PER_PAGE
                mImageAdapter =
                    ImageListAdapter(
                        this.photo as ArrayList<Photo>,
                        this.pages.toInt() * ITEM_PER_PAGE
                    )
                rv.adapter = mImageAdapter
                // configuration handling
                mViewModel.mImageData.addAll(this.photo)
                return@renderUI
            }
            mImageAdapter.addData(this.photo as ArrayList<Photo>)
            // configuration handling
            mViewModel.mImageData.addAll(this.photo)
            return@renderUI
        }
        Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT)
            .show()
    }

    private fun initObserver() {
        if (!this::mImageDataObserver.isInitialized) {
            mImageDataObserver = Observer {

                // disable progress state view toogle
                pb.visibility = View.INVISIBLE
                txt_info.visibility = View.INVISIBLE
                rv.visibility = View.VISIBLE

                when (it) {
                    is Success -> {
                        renderUI(it.response as ImageSearchResponse)
                        mViewModel.mNextPage++
                    }
                    is InternetNotConnected -> {
                        Toast.makeText(
                            this,
                            getString(R.string.internet_not_connected),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Fail -> {
                        Toast.makeText(
                            this,
                            getString(R.string.something_went_wrong),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}