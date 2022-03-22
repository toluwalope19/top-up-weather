package com.example.top_up_weather.utils

//data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
//
//    companion object {
//        fun <T> success(data: T?): Resource<T> {
//            return Resource(Status.SUCCESS, data, null)
//        }
//
//        fun <T> error( error: Throwable, data: T? = null): Resource<T> {
//            return Resource(Status.ERROR, data,error.message)
//        }
//
//        fun <T> loading(data: T? = null): Resource<T> {
//            return Resource(Status.LOADING, data, null)
//        }
//
//    }
//
//    enum class Status {
//        SUCCESS,
//        ERROR,
//        LOADING
//    }
//}

sealed class Resource<T>(
    val status: Status,
    val data: T?,
    val error: Throwable? = null
) {

    class Success<T>(data: T): Resource<T>(Status.SUCCESS,data,null)
    class Loading<T>(data: T? = null): Resource<T>(Status.LOADING,data,null)
    class Error<T>(throwable: Throwable,data: T? = null): Resource<T>(Status.ERROR,data,throwable)

}
