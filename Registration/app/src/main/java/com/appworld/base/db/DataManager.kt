package com.flocks.address.user.cache

import com.appworld.base.db.UserProfile

interface DataManager {

    fun verifyUserLogin(userName: String, password: String) : Boolean

    fun persistUserProfile(userProfile: UserProfile)
}