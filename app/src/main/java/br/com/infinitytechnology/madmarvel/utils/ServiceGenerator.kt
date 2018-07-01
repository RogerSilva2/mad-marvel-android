package br.com.infinitytechnology.madmarvel.utils

import android.content.Context
import android.text.TextUtils

import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {

    fun <S> createService(context: Context, serviceClass: Class<S>): S {
        return createService(context, serviceClass, null, null)
    }

    private fun <S> createService(context: Context, serviceClass: Class<S>, username: String?,
                                  password: String?): S {
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            val authToken = Credentials.basic(username, password)
            return createService(context, serviceClass, authToken)
        }

        return createService(context, serviceClass, null)
    }

    private fun <S> createService(context: Context, serviceClass: Class<S>,
                                  authToken: String?): S {
        val apiBaseUrl = PropertyUtil.property(context, "api.base.url")

        val httpClient = OkHttpClient.Builder()
        val builder = Retrofit.Builder().baseUrl(apiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
        var retrofit = builder.build()

        if (!TextUtils.isEmpty(authToken)) {
            val interceptor = AuthenticationInterceptor(authToken!!)

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor)

                builder.client(httpClient.build())
                retrofit = builder.build()
            }
        }

        return retrofit.create(serviceClass)
    }
}