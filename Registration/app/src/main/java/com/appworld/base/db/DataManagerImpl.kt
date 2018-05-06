package com.flocks.address.user.cache

import com.appworld.base.BaseApp
import com.appworld.base.db.DatabaseRealm
import com.appworld.base.db.UserProfile
import javax.inject.Inject

class DataManagerImpl(baseApp: BaseApp) : DataManager {
    init {
        baseApp.appComponent.inject(this)
    }

    @Inject
    lateinit var databaseRealm: DatabaseRealm

    override fun verifyUserLogin(userName: String, password: String): Boolean {
        return databaseRealm.findUser(UserProfile::class.java, userName = userName, password = password).isNotEmpty()
    }

    override fun persistUserProfile(userProfile: UserProfile) {
        databaseRealm.add(userProfile)
    }
}