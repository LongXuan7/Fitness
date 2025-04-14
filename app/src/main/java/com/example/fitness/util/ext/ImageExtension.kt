package com.example.fitness.util.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.fitness.R

fun ImageView.loadImage(url: String?) {
     Glide.with(this.context)
         .load(url)
//         .placeholder(R.drawable.placeholder)
         .into(this)
}