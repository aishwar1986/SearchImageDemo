package com.demo.searchimagedemo.repository


/**
 * generic class hold response
 */
sealed class Result<out R>
data class Success<out T>(val response: T) : Result<T>()
data class Fail(val error: Throwable) : Result<Nothing>()
object InternetNotConnected:Result<Nothing>()