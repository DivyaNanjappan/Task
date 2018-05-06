package com.appworld.base.di.component

import com.appworld.base.BaseApp
import com.appworld.base.db.DatabaseRealm
import com.appworld.base.di.module.AppModule
import com.appworld.base.di.module.CacheModule
import com.flocks.address.user.cache.DataManagerImpl
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (CacheModule::class)])
interface AppComponent {

    fun inject(baseApp: BaseApp) {}

    fun inject(databaseRealm: DatabaseRealm)

    fun inject(dataManagerImpl: DataManagerImpl)

}