package com.spacece.milestonetracker.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.spacece.milestonetracker.data.model.User

class SharedPrefs constructor(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

    fun saveUserDetails(user: User?) {
        user ?: return
        prefs.edit {
            putString(USER_TYPE, user.current_user_type)
            putString(AUTH_TOKEN, user.token)
            putString("USER_EMAIL", user.current_user_email)
            putString(KEY_USER_ID, user.current_user_id.toString())
            putBoolean(IS_USER_LOG_IN, true)
        }
    }

    fun getEmail(): String {
        return prefs.getString("USER_EMAIL", "") ?: ""
    }

    fun clearAllPrefs() {
        prefs.edit { clear() }
    }

    fun setUserType(userType: String) = prefs.edit { putString(USER_TYPE, userType) }
    fun getUserType(): String = prefs.getString(USER_TYPE, "") ?: ""
    fun getAuthToken(): String = prefs.getString(AUTH_TOKEN, "") ?: ""

    fun setUserLoggedIn(isLoggedIn: Boolean) = prefs.edit { putBoolean(IS_USER_LOG_IN, isLoggedIn) }
    fun isUserLoggedIn(): Boolean = prefs.getBoolean(IS_USER_LOG_IN, false)

    fun setGuestLoggedIn(isLoggedIn: Boolean) =
        prefs.edit { putBoolean(IS_GUEST_LOG_IN, isLoggedIn) }

    fun isGuestLoggedIn(): Boolean = prefs.getBoolean(IS_GUEST_LOG_IN, false)


    fun saveSelectedChildId(childId: String) {
        prefs.edit().putString("SELECTED_CHILD_ID", childId).apply()
    }

    fun getSelectedChildId(): String {
        return prefs.getString("SELECTED_CHILD_ID", "") ?: ""
    }

    fun saveUserId(userId: String) {
        if (userId.isEmpty() || userId == "null") return
        prefs.edit().putString(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): String {
        return prefs.getString(KEY_USER_ID, "") ?: ""
    }
}
