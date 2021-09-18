package com.random.user.domain

class UserFetchError(message: String, cause: Throwable) : RuntimeException(message, cause)