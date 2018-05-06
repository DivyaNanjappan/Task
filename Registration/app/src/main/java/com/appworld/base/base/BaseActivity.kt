package com.appworld.base.base

import android.support.v4.content.ContextCompat
import android.view.WindowManager
import com.appworld.base.di.component.NetComponent
import nucleus.presenter.RxPresenter

open class BaseActivity<T : RxPresenter<*>> : AbstractBaseActivity<T>() {
    override fun injectComponent(component: NetComponent) {
    }

    fun showActionBar() {
        supportActionBar!!.show()
    }

    fun hideActionBar() {
        supportActionBar!!.hide()
    }

    fun hideKeyBoard(){
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    fun getColorValue(color: Int) : Int{
        return ContextCompat.getColor(this, color)
    }
}
