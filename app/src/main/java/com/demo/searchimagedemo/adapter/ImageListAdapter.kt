package com.demo.searchimagedemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.demo.searchimagedemo.R
import com.demo.searchimagedemo.model.data.Photo
import com.demo.searchimagedemo.utils.Logger

class ImageListAdapter constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // view types
    private val VIEW_TYPE_LOADING = 1
    private val VIEW_TYPE_ITEM = 0

    private var mData: ArrayList<Photo>? = null

    // total data items at remote side
    private var mTotalCount = 0

    constructor(
        data: ArrayList<Photo>,
        totalCount: Int
    ) : this() {
        mData = data
        mTotalCount = totalCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        when (viewType) {
            VIEW_TYPE_LOADING -> {
                return LoadMoreViewHolder(
                    layoutInflater.inflate(
                        R.layout.item_image_loading,
                        parent,
                        false
                    )
                )
            }
        }
        return ImageViewHolder(layoutInflater.inflate(R.layout.item_image, parent, false))

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ImageViewHolder) {
            Glide.with(holder.img.context).load(mData?.get(position)?.getImgUrl()).apply(
                RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA)
                    .dontAnimate()
                    .placeholder(R.drawable.all_round_gray)
                    .error(R.drawable.all_round_gray)
            ).into(holder.img)
        }
    }

    override fun getItemCount(): Int {
        if (mData?.size?.compareTo(mTotalCount) != 0) {
            return mData?.size?.plus(1) ?: 0
        }
        return mData?.size ?: 0
    }


    fun addData(data: ArrayList<Photo>) {
        if (mData == null) {
            mData = data
        } else {
            mData?.addAll(data)
        }
        notifyDataSetChanged()
    }

    fun clearData() {
        mData?.clear()
        mTotalCount=0
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val size = mData?.size
        Logger.debugLog(pMsg = "totalCount: $mTotalCount, DataSize: $size")
        if (size?.compareTo(mTotalCount) != 0 && position == mData?.size) {
            return VIEW_TYPE_LOADING
        }
        return VIEW_TYPE_ITEM
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView = itemView.findViewById(R.id.item_img)
    }


    /**
     * view holder for load more view (pagination)
     */
    inner class LoadMoreViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}