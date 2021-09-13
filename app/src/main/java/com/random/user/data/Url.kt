package com.random.user.data

import javax.inject.Inject

class Url @Inject constructor(private val url: String) {
    fun getUrl() = url
}