package com.appworld.base

import android.support.multidex.MultiDexApplication
import com.appworld.base.db.DatabaseRealm
import com.appworld.base.di.component.AppComponent
import com.appworld.base.di.component.DaggerAppComponent
import com.appworld.base.di.component.DaggerNetComponent
import com.appworld.base.di.component.NetComponent
import com.appworld.base.di.module.AppModule
import com.appworld.base.di.module.CacheModule
import javax.inject.Inject


class BaseApp : MultiDexApplication() {
    lateinit var appComponent: AppComponent
    lateinit var netComponent: NetComponent

    @Inject
    lateinit var realm: DatabaseRealm


    override fun onCreate() {
        super.onCreate()

        initComponents()

        initRealm()
    }

    private fun initComponents() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .cacheModule(CacheModule(this))
                .build()

        netComponent = DaggerNetComponent.builder()
                .appModule(AppModule(this))
                .cacheModule(CacheModule(this))
                .build()

        appComponent.inject(this)
    }

    private fun initRealm() {
        realm.setup()
    }
}