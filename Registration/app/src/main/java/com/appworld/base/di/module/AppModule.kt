package com.appworld.base.di.module

import android.content.Context
import com.appworld.base.BaseApp
import com.appworld.base.db.DatabaseRealm
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private var baseApp: BaseApp) {

    @Provides
    @Singleton
    fun providesApplication(): BaseApp {
        return baseApp
    }

    @Provides
    @Singleton
    fun applicationContext(): Context {
        return baseApp.applicationContext
    }

    @Provides
    @Singleton
    fun provideDatabaseRealm(): DatabaseRealm {
        return DatabaseRealm(baseApp)
    }
}