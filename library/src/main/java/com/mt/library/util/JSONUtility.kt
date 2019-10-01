package com.mt.library.util

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JSONUtility {
    fun isJSONValid(string: String): Boolean {
        try {
            JSONObject(string)
        } catch (ex: JSONException) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                JSONArray(string)
            } catch (ex1: JSONException) {
                return false
            }

        }

        return true
    }
}