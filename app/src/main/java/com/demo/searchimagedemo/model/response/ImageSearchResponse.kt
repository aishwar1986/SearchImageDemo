package com.demo.searchimagedemo.model.response

import com.demo.searchimagedemo.model.data.Photo
import com.demo.searchimagedemo.model.data.Photos

data class ImageSearchResponse(val photos: Photos,val stat: String)

