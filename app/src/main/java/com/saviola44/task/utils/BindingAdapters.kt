package com.saviola44.task.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.saviola44.task.R
import com.squareup.picasso.Picasso

object BindingAdapters{
    @JvmStatic
    @BindingAdapter("avatar")
    fun setImageUrl(v: ImageView, url: String?) {

        Picasso.get()
            .load(url)
            .placeholder(R.drawable.placeholder)
            .into(v)
    }
}
