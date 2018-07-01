package br.com.infinitytechnology.madmarvel.utils

import android.content.Context
import android.util.Log
import br.com.infinitytechnology.madmarvel.R
import java.io.IOException
import java.util.*

object PropertyUtil {

    fun property(context: Context, key: String): String {
        return try {
            val properties = Properties()
            val assetManager = context.assets
            val inputStream = assetManager.open("config.properties")
            properties.load(inputStream)
            properties.getProperty(key)
        } catch (e: IOException) {
            Log.e(context.getString(R.string.app_name), context.getString(R.string.error_getting_property), e)
            ""
        }
    }
}