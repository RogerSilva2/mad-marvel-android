package br.com.infinitytechnology.madmarvel.utils

import android.content.Context
import android.util.Log

import java.io.IOException
import java.util.Properties

import br.com.infinitytechnology.madmarvel.R

object PropertyUtil {

    fun property(context: Context, key: String): String {
        try {
            val properties = Properties()
            val assetManager = context.assets
            val inputStream = assetManager.open("config.properties")
            properties.load(inputStream)
            return properties.getProperty(key)
        } catch (e: IOException) {
            Log.e(context.getString(R.string.app_name), context.getString(R.string.error_getting_property), e)
            return ""
        }
    }
}