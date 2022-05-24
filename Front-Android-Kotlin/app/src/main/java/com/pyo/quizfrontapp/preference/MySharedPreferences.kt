package com.pyo.quizfrontapp.preference

import android.content.Context
import android.content.SharedPreferences

object MySharedPreferences {
    private val MY_NICKNAME : String = "nickName"
    fun setUserId(context: Context, input: String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_NICKNAME, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("MY_ID", input)
        editor.commit()
    }

    fun getUserId(context: Context): String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_NICKNAME, Context.MODE_PRIVATE)
        return prefs.getString("MY_ID", "").toString()
    }


}