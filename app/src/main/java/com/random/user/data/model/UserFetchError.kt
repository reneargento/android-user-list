package com.random.user.data.model

class UserFetchError(message: String, cause: Throwable) : RuntimeException(message, cause)