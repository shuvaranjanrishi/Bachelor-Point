package com.therishideveloper.bachelorpoint.session

import android.content.Context
import com.therishideveloper.bachelorpoint.utils.Keys
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Created by Shuva Ranjan Rishi on 06,July,2023
 * BABL, Bangladesh,
 */

class UserSession @Inject constructor(@ApplicationContext context: Context) {

    private var pref = context.getSharedPreferences(Keys.USER_SESSION, Context.MODE_PRIVATE)
    private var editor = pref.edit()

    fun setUid(uid: String) {
        editor.putString(Keys.USER_ID, uid)
        editor.apply()
    }

    fun getUid(): String? {
        return pref.getString(Keys.USER_ID, "")
    }

    fun setId(id: String) {
        editor.putString(Keys.ID, id)
        editor.apply()
    }

    fun getId(): String? {
        return pref.getString(Keys.ID, "")
    }

    fun setAccountId(uid: String) {
        editor.putString(Keys.ACCOUNT_ID, uid)
        editor.apply()
    }

    fun getAccountId(): String? {
        return pref.getString(Keys.ACCOUNT_ID, "")
    }

    fun setUserType(uid: String) {
        editor.putString(Keys.USER_TYPE, uid)
        editor.apply()
    }

    fun getUserType(): String? {
        return pref.getString(Keys.USER_TYPE, "")
    }

    fun setUserName(uid: String) {
        editor.putString(Keys.USER_NAME, uid)
        editor.apply()
    }

    fun getUserName(): String? {
        return pref.getString(Keys.USER_NAME, "")
    }

    fun setEmail(uid: String) {
        editor.putString(Keys.EMAIL, uid)
        editor.apply()
    }

    fun getEmail(): String? {
        return pref.getString(Keys.EMAIL, "")
    }

    fun deleteSession() {
        return editor.clear().apply()
    }
}
