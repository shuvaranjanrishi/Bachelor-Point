package com.therishideveloper.bachelorpoint.session

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

/**
 * Created by Shuva Ranjan Rishi on 06,July,2023
 * BABL, Bangladesh,
 */

class SessionManager(context: Context) {

    private val TAG = "UserSession"

    private val sharedPref: SharedPreferences
    private val editor: SharedPreferences.Editor
    private val context: Context

    private val PREFERENCE_NAME = "SessionManager"
    private val KEY_IS_LOGGED_IN = "isLoggedIn"
    private val KEY_NAME = "name"
    private val KEY_ID = "id"
    private val KEY_USER_ID = "userId"
    private val KEY_USER_NAME = "username"
    private val KEY_EMAIL = "email"
    private val KEY_USER_TYPE = "userType"
    private val KEY_ACCOUNT_ID = "accountId"
    val KEY_PHONE = "phone"
    private val KEY_ADDRESS = "address"
    private val KEY_APK_VERSION = "apk_version"

    init {
        this.context = context
        sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    fun getUserId(): String? {
        return sharedPref.getString(KEY_USER_ID, "0")
    }

    fun setUserId(value: String?) {
        editor.putString(KEY_USER_ID, value)
        editor.commit()
    }

    fun getUserType(): String? {
        return sharedPref.getString(KEY_USER_TYPE, "0")
    }

    fun setUserType(userType: String?) {
        editor.putString(KEY_USER_TYPE, userType)
        editor.commit()
    }

    fun getId(): String? {
        return sharedPref.getString(KEY_ID, "0")
    }

    fun setId(value: String?) {
        editor.putString(KEY_ID, value)
        editor.commit()
    }

    fun getAccountId(): String? {
        return sharedPref.getString(KEY_ACCOUNT_ID, "0")
    }

    fun setAccountId(accountId: String?) {
        editor.putString(KEY_ACCOUNT_ID, accountId)
        editor.commit()
    }

    fun setUsername(username: String?) {
        editor.putString(KEY_USER_NAME, username)
        editor.commit()
    }

    fun getUserName(): String? {
        return sharedPref.getString(KEY_USER_NAME, "0")
    }

    fun setName(name: String?) {
        editor.putString(KEY_NAME, name)
        editor.commit()
    }

    fun getName(): String? {
        return sharedPref.getString(KEY_NAME, "0")
    }

    fun getAddress(): String? {
        return sharedPref.getString(KEY_ADDRESS, "")
    }

    fun setEmail(email: String?) {
        editor.putString(KEY_EMAIL, email)
        editor.commit()
    }

    fun getEmail(): String? {
        return sharedPref.getString(KEY_EMAIL, "")
    }

    fun getIsLoggedIn(): Boolean {
        return sharedPref.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun deleteSession() {
        editor.clear()
        editor.apply()
    }

    fun logoutUser() {
        editor.clear()
        editor.commit()
    }

    fun getApkVersion(): String? {
        return sharedPref.getString(KEY_APK_VERSION, "")
    }

    fun setApkVersion(apkVersion: String?) {
        editor.putString(KEY_APK_VERSION, apkVersion)
        editor.commit()
    }

    fun setApiIssue(dataJson: String?) {
        editor.putString("apiIssue", dataJson)
        editor.commit()
    }

}
