package com.appworld.base.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.appworld.base.BaseApp
import com.appworld.base.R
import com.appworld.base.base.BaseActivity
import com.appworld.base.di.annotation.ViewObservables
import com.appworld.base.di.component.NetComponent
import com.appworld.base.util.ObserverState
import com.jakewharton.rxbinding.view.RxView
import com.jakewharton.rxbinding.widget.RxTextView
import com.orhanobut.logger.Logger
import nucleus.factory.RequiresPresenter
import rx.Observer

@RequiresPresenter(LoginViewPresenter::class)
class LoginScreen : BaseActivity<LoginViewPresenter>(), ViewObservables {
    private lateinit var editTextName: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonRegister: Button

    override fun injectComponent(component: NetComponent) {
        component.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* This might be needed on every activity */
        val superFactory = super.getPresenterFactory()
        setPresenterFactory {
            val presenter = superFactory.createPresenter()
            (application as BaseApp).netComponent.inject(presenter)
            presenter
        }

        setContentView(R.layout.activity_login_screen)

        presenter.setupRealm()

        initView()

        registerViewObservables()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.closeRealm()
    }

    private fun initView() {
        editTextName = findViewById(R.id.editTextUserName)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonRegister = findViewById(R.id.buttonRegister)
    }

    private fun navigateToRegister() {
        editTextPassword.text.clear()
        editTextName.text.clear()

        val registerIntent = Intent(this, RegisterActivity::class.java)
        startActivity(registerIntent)
    }

    private fun navigateToLandingScreen() {
        val registerIntent = Intent(this, LandingActivity::class.java)
        startActivity(registerIntent)
        finish()
    }

    override fun registerViewObservables() {
        val userNameObservable = RxTextView.textChanges(editTextName)
        val passwordObservable = RxTextView.textChanges(editTextPassword)

        presenter.attachObservables(userNameObservable, passwordObservable)

        presenter.validateUserInputs!!.subscribe(object : Observer<Boolean> {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(result: Boolean?) {
                Logger.d(String.format("%b", result))
                when (result) {
                    true -> {
                        buttonLogin.isEnabled = result
                        buttonLogin.setTextColor(getColorValue(R.color.white))
                    }
                    else -> {
                        result?.let { buttonLogin.isEnabled = it }
                        buttonLogin.setTextColor(getColorValue(R.color.app_placeholder))
                    }
                }
            }
        })

        RxView.clicks(buttonLogin)
                .subscribe {
                    presenter.rx_onUpdate.onNext(ObserverState.ON_LOGIN)
                }

        RxView.clicks(buttonRegister)
                .subscribe {
                    presenter.rx_onUpdate.onNext(ObserverState.ON_REGISTER)
                }

        //On Error
        presenter.rx_onError
                .subscribe(object : Observer<Exception> {
                    override fun onCompleted() {
                        //Do nothing
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onNext(e: Exception) {
                        e.message?.let { showToast(it) }
                    }
                })

        //On Update
        presenter.rx_onUpdate
                .subscribe(object : Observer<ObserverState> {
                    override fun onCompleted() {
                        //Do nothing
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onNext(state: ObserverState) {

                        when (state) {

                            ObserverState.ON_LOGIN -> {
                                if(presenter.isValidUser()){
                                    navigateToLandingScreen()
                                } else {
                                    showToast("User not found")
                                }
                            }

                            ObserverState.ON_REGISTER -> navigateToRegister()

                            else -> "Do Nothing"
                        }
                    }
                })
    }
}
