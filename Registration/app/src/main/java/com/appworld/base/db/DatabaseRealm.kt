package com.appworld.base.db

import android.content.Context
import com.appworld.base.BaseApp
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject
import io.realm.exceptions.RealmFileException
import io.realm.exceptions.RealmMigrationNeededException

class DatabaseRealm(baseApp: BaseApp) {

    init {
        baseApp.appComponent.inject(this)
    }

    private var mContext: Context = baseApp.applicationContext

    fun setup() {
        if (realmConfiguration == null) {
            //Init realm
            Realm.init(mContext)
            //Init realm configuration

            //this local instance needed to force the migration if there is any schema changes in place
            //Upon migration exception, force delete all existing schema and recreate new tables for the app.
            //finally close local instance access to realm.
            var localInstance: Realm? = null
            try {
                realmConfiguration = RealmConfiguration.Builder()
                        .build()
                Realm.setDefaultConfiguration(realmConfiguration!!)
                localInstance = Realm.getDefaultInstance()
            } catch (rfe: RealmFileException) {

            } catch (r: RealmMigrationNeededException) {

            } catch (exp: Exception) {

            } finally {
                if (localInstance != null) {
                    if (!localInstance.isClosed) {
                        localInstance.close()
                    }
                }
            }
        } else {
            throw IllegalStateException("database already configured")
        }
    }

    private fun getRealmInstance(): Realm {
        if (realm != null) {
            return realm as Realm
        }
        realm = Realm.getDefaultInstance()
        return realm!!
    }

    fun <T : RealmObject> findUser(clazz: Class<T>, userName: String, password: String): List<T> {
        return getRealmInstance().where(clazz).equalTo("userName", userName).equalTo("password", password).findAll()
    }

    fun <T : RealmObject> findAll(clazz: Class<T>): List<T> {
        return getRealmInstance().where(clazz).findAll()
    }

    fun <T : RealmObject> findById(clazz: Class<T>, id: String): T? {
        return getRealmInstance().where(clazz).equalTo("id", id).notEqualTo("id", "").findFirst()
    }

    fun <T : RealmObject> add(model: T) {
        var realm: Realm? = null
        try {
            realm = Realm.getDefaultInstance()
            realm!!.executeTransaction { realm -> realm.insertOrUpdate(model) }
        } finally {
            if (realm != null) {
                realm.close()
            }
        }
    }

    fun <T : RealmObject> delete(model: T) {
        var realm: Realm? = null
        try {
            realm = Realm.getDefaultInstance()
            realm!!.executeTransaction { model.deleteFromRealm() }
        } finally {
            if (realm != null) {
                realm.close()
            }
        }
    }

    companion object {
        var realm: Realm? = null
        internal var realmConfiguration: RealmConfiguration? = null
    }
}
