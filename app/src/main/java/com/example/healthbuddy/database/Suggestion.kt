package com.example.healthbuddy.database

import android.graphics.Bitmap

data class Suggestion(
    val postId:String?=null,
    val postTitle:String?=null,
    val postDescription:String?=null,
    val postImage: Bitmap?=null
)