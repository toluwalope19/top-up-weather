package com.example.top_up_weather.utils
//
//data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
//
//    companion object {
//        fun <T> success(data: T?): Resource<T> {
//            return Resource(Status.SUCCESS, data, null)
//        }
//
//        fun <T> error(msg: String, data: T? = null): Resource<T> {
//            return Resource(Status.ERROR, data, msg)
//        }
//
//        fun <T> loading(data: T? = null): Resource<T> {
//            return Resource(Status.LOADING, data, null)
//        }
//
//        fun <T> normal(): Resource<T> {
//            return Resource(Status.Normal, null, null)
//        }
//    }
//
//    enum class Status {
//        SUCCESS,
//        ERROR,
//        LOADING,
//        Normal
//    }
//
//
//}

//   enum class Status {
//       SUCCESS,
//      ERROR,
//       LOADING,
//       Normal
//   }

sealed class Resource<T>(

    val data: T? = null,
    val message: String? = null
) {
    data class Success<T>(val t: T) : Resource<T>(t)
    data class Loading<T>(val t: T? = null) : Resource<T>(t)
    data class Error<T>(val error: Throwable, val t: T? = null) : Resource<T>(t, error.message)

}
