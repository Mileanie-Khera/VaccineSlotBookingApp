package com.study.preferrences

import android.content.Context
import android.content.SharedPreferences

class CowinSharedPrefs {

    companion object {
        private var mContext: Context? = null
        var mSharedPreferences: SharedPreferences? = null
        const val COWIN_SHARED_PREFS = "COWIN_SHARED_PREFS"

        const val KEY_BEARER_TOKEN = "KEY_BEARER_TOKEN"
        const val KEY_PIN_CODE = "KEY_PIN_CODE"
        const val KEY_DATE = "KEY_DATE"
        const val KEY_VACCINE_TYPE = "KEY_VACCINE_TYPE"
        const val KEY_FEE_TYPE = "KEY_FEE_TYPE"
        const val KEY_AGE = "KEY_AGE"
        const val KEY_TIME_DELAY = "KEY_TIME_DELAY"

        @JvmStatic
        fun getIntstance(context: Context): CowinSharedPrefs {
            val cowinSharedPrefs = CowinSharedPrefs()
            mContext = context
            if (mSharedPreferences == null)
                mSharedPreferences =
                    mContext?.getSharedPreferences(COWIN_SHARED_PREFS, Context.MODE_PRIVATE)

            return cowinSharedPrefs
        }
    }

    fun storeStringPref(key: String, value: String) {
        val editor = mSharedPreferences?.edit()
        editor?.putString(key, value)
        editor?.commit()
    }

    fun getStringPref(key: String): String {
        return mSharedPreferences?.getString(key, "") ?: ""
    }

    fun storeLongPref(key: String, value: Long) {
        val editor = mSharedPreferences?.edit()
        editor?.putLong(key, value)
        editor?.commit()
    }

    fun getLongPref(key: String): Long {
        return mSharedPreferences?.getLong(key, 0L) ?: 0L
    }
}