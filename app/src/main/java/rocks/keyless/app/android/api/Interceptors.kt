package rocks.keyless.app.android.api

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import rocks.keyless.app.android.util.Util

class TokenValidationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var isAuthTokenValid: Boolean
        return runBlocking(Dispatchers.IO) {
            isAuthTokenValid = Util.getAccessToken() != ""
            if (isAuthTokenValid) {
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${Util.getAccessToken()}")
                    .build()
                chain.proceed(request)
            } else {
                Response.Builder()
                    .request(chain.request())
                    .code(401)
                    .message("Invalid token")
                    .body("Invalid token".toResponseBody(null))
                    .protocol(Protocol.HTTP_1_1)
                    .build()
            }
        }
    }
}

class HostSelectionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val httpUrl = Util.BASE_URL.toHttpUrl()
        val newUrl = request.url.newBuilder().host(httpUrl.host).build()
        val newRequest = request.newBuilder().url(newUrl).build()
        return chain.proceed(newRequest)
    }
}

class ConnectivityInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (Util.isNetworkAvail(context)) {
            Log.i("Network Interceptor", "Valid Network to call API")
            return chain.proceed(chain.request())
        }
        Log.i("Network Interceptor", "Invalid Network to call API")

        return Response
            .Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .code(503)
            .message("Check your internet Connection")
            .body("Invalid Network to call API".toResponseBody(null))
            .build()
    }
}