package com.yelp.yelper

import android.content.Context
import androidx.annotation.StringRes

class StringResourceProvider(private val context: Context) {

    fun getString(@StringRes stringResId: Int): String = context.getString(stringResId)
}