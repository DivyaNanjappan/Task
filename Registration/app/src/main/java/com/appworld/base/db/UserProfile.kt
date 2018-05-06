package com.appworld.base.db

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

@RealmClass
open class UserProfile: RealmObject() {

    @PrimaryKey
    @Required
    var id: Int? = 1

    var email: String? = null

    var userName: String? = null

    var password: String? = null

    var mobileNumber: String? = null
}