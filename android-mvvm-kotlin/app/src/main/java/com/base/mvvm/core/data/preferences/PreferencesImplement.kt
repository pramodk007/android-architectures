package com.base.mvvm.core.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.base.mvvm.core.utilities.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Author: William Giang Nguyen | 8/7/2022
 * */
@Singleton
class PreferencesImplement @Inject constructor(
    @ApplicationContext context: Context
) : RxPreferences {

    private val mPrefs: SharedPreferences = context.getSharedPreferences(
        Constants.PREF_FILE_NAME,
        Context.MODE_PRIVATE
    )

    override fun put(key: String, value: String) {
        val editor: SharedPreferences.Editor = mPrefs.edit()
        editor.putString(key, value)
        editor.apply()
    }

    override fun get(key: String): String? {
        return mPrefs.getString(key, "")
    }

    override fun clear() {
        val editor: SharedPreferences.Editor = mPrefs.edit()
        editor.clear()
        editor.apply()
    }

    override fun remove(key: String) {
        val editor: SharedPreferences.Editor = mPrefs.edit()
        editor.remove(key)
        editor.apply()
    }

    override fun getToken() = get(PREF_PARAM_USER_INFO)

    override fun setUserToken(userToken: String) = put(PREF_PARAM_USER_INFO, userToken)

    override fun logout() {
        remove(PREF_PARAM_USER_INFO)
        remove(PREF_KEY_IS_FIRST_LOGIN)
        clear()
    }

    override fun isLogin(): Boolean {
        return mPrefs.getBoolean(PREF_KEY_IS_FIRST_LOGIN, false)
    }

    override fun setLogin(isLogin: Boolean) {
        mPrefs.edit().putBoolean(PREF_KEY_IS_FIRST_LOGIN, isLogin).apply()
    }

}

private const val PREF_PARAM_USER_INFO = "PREF_PARAM_USER_INFO"

private const val PREF_KEY_IS_FIRST_LOGIN = "PREF_KEY_IS_FIRST_LOGIN"