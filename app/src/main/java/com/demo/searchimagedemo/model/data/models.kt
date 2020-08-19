package com.demo.searchimagedemo.model.data

data class Photo(val id: String, val secret: String, val server: String, val farm: String) {
    fun getImgUrl() = "https://farm$farm.staticflickr.com/$server/$id"+"_"+ secret +"_q.png"
}

data class Photos(val page: String, val pages: String, val perpage: String, val photo: List<Photo>)