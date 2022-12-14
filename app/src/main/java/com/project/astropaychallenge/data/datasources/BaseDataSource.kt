package com.project.astropaychallenge.data.datasources

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.io.Serializable
import java.net.ConnectException
import java.net.UnknownHostException

abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.success(body)
                } else {
                    error("Code ${response.code()}: ${response.message()}")
                }
            } else {
                error("Code ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is IOException -> "No internet connection"
                is HttpException -> "Something went wrong!"
                is UnknownHostException -> "Unknown host!"
                is ConnectException -> "No internet!"
                else -> e.localizedMessage
            }
            error(errorMessage)
        }
    }
}

/**
 * Encapsulate repository's response according to its state
 */
data class Resource<out T>(val status: Status, val data: T?, val message: String?) : Serializable {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                null
            )
        }

        fun <T> error(message: String, data: T? = null): Resource<T> {
            return Resource(
                Status.ERROR,
                data,
                message
            )
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(
                Status.LOADING,
                data,
                null
            )
        }
    }
}