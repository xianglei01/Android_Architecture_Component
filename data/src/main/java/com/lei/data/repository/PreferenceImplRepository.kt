/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.lei.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson

/**
 * @author xianglei
 * @created 2018/7/10 12:22
 *
 *
 * SharePreference缓存实现类
 */
class PreferenceImplRepository(context: Context, private val gson: Gson) : PreferenceRepository {

    private val mPrefs: SharedPreferences

    init {
        mPrefs = context.getSharedPreferences(PRE_FILE, Context.MODE_PRIVATE)
    }

    /**
     * 添加缓存String
     */
    private fun putString(key: String, value: String?) {
        if (value == null) {
            remove(key)
            return
        }
        mPrefs.edit().putString(key, value).apply()
    }

    /**
     * 取缓存
     * 拿到缓存解密
     */
    private fun getString(key: String, def: String): String {
        var value = mPrefs.getString(key, "")
        return if (TextUtils.isEmpty(value)) def else value
    }

    private fun putInt(key: String, value: Int) {
        putString(key, value.toString())
    }

    private fun putLong(key: String, value: Long) {
        putString(key, value.toString())
    }

    private fun putBoolean(key: String, value: Boolean) {
        putString(key, value.toString())
    }

    private fun getInt(key: String): Int {
        return try {
            Integer.parseInt(getString(key, "0"))
        } catch (e: Exception) {
            0
        }
    }

    private fun getLong(key: String, def: Long): Long? {
        return try {
            getString(key, "0").toLong()
        } catch (e: Exception) {
            def
        }
    }

    private fun getBoolean(key: String, def: Boolean): Boolean {
        return try {
            getString(key, def.toString()).toBoolean()
        } catch (e: Exception) {
            def
        }
    }

    private fun remove(key: String) {
        mPrefs.edit().remove(key).apply()
    }

    companion object {

        private const val PRE_FILE = "pre_demo"
        private var preferenceImplRepository: PreferenceImplRepository? = null

        fun getPre(context: Context): PreferenceImplRepository {
            if (preferenceImplRepository == null) {
                preferenceImplRepository = PreferenceImplRepository(context, Gson())
            }
            return preferenceImplRepository!!
        }
    }
}
