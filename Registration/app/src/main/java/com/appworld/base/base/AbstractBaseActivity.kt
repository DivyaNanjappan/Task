package com.appworld.base.base

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.appworld.base.BaseApp
import com.appworld.base.di.component.NetComponent
import nucleus.presenter.RxPresenter
import nucleus.view.NucleusAppCompatActivity

abstract class AbstractBaseActivity<T : RxPresenter<*>> : NucleusAppCompatActivity<T>() {
    var baseApp: BaseApp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        baseApp = applicationContext as BaseApp
        injectComponent(baseApp!!.netComponent)
    }

    abstract fun injectComponent(component: NetComponent)

    fun showProgressDialog() {
        if (!isFinishing) {
        }
    }

    fun updateProgressDialog(message: String) {
        if (!isFinishing) {
        }
    }

    fun dismissProgressDialog() {
    }

    fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }
 }
