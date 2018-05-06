package com.appworld.base.di.component

import com.appworld.base.di.module.AppModule
import com.appworld.base.di.module.CacheModule
import com.appworld.base.main.LandingActivity
import com.appworld.base.main.LoginScreen
import com.appworld.base.main.LoginViewPresenter
import com.appworld.base.main.RegisterActivity
import com.flocks.address.user.cache.DataManager
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class), (CacheModule::class)])
interface NetComponent {

    fun inject(dataManager: DataManager)

    fun inject(loginScreen: LoginScreen)

    fun inject(registerActivity: RegisterActivity)

    fun inject(landingActivity: LandingActivity)

    fun inject(loginViewPresenter: LoginViewPresenter)
}