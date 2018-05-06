package com.appworld.base.di.module

import com.appworld.base.BaseApp
import com.flocks.address.user.cache.DataManager
import com.flocks.address.user.cache.DataManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CacheModule(var baseApp: BaseApp) {

    @Provides
    @Singleton
    fun provideDataManager(): DataManager {
        return DataManagerImpl(baseApp)
    }

}