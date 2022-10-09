package com.example.demo

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.demo.network.data.Attractions


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, attractions: Attractions?) {
    val adapter = recyclerView.adapter as MainAdapter
    adapter.submitList(attractions?.data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(RequestOptions())
            .into(imgView)
    }
}