package com.example.quizapp19

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

object QuestionUtils {

    fun loadQuestions(context: Context, filename: String): List<Question> {
        val json = loadJSONFromAsset(context, filename) ?: return emptyList()

        val listType = object : TypeToken<List<Question>>() {}.type
        return Gson().fromJson(json, listType)
    }

    private fun loadJSONFromAsset(context: Context, filename: String): String? {
        return try {
            val inputStream: InputStream = context.assets.open(filename)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }
}
