package com.appworld.base.main

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
class RegisterActivity : BaseActivity<LoginViewPresenter>(), ViewObservables {
    private lateinit var editTextName: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextMobileNo: EditText
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

        setContentView(R.layout.activity_register)

        initView()

        registerViewObservables()
    }

    private fun initView() {
        editTextName = findViewById(R.id.editTextUserName)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextMobileNo = findViewById(R.id.editTextMobileNo)
        buttonRegister = findViewById(R.id.buttonRegister)
    }

    override fun registerViewObservables() {
        val userNameObservable = RxTextView.textChanges(editTextName)
        val passwordObservable = RxTextView.textChanges(editTextPassword)
        val emailObservable = RxTextView.textChanges(editTextEmail)
        val mobileNoObservable = RxTextView.textChanges(editTextMobileNo)

        presenter.attachObservables(userNameObservable, passwordObservable, emailObservable, mobileNoObservable)

        presenter.validateUserInputs!!.subscribe(object : Observer<Boolean> {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(result: Boolean?) {
                Logger.d(String.format("%b", result))
                when (result) {
                    true -> {
                        buttonRegister.isEnabled = result
                        buttonRegister.setTextColor(getColorValue(R.color.white))
                    }
                    else -> {
                        result?.let { buttonRegister.isEnabled = it }
                        buttonRegister.setTextColor(getColorValue(R.color.app_placeholder))
                    }
                }
            }
        })

        RxView.clicks(buttonRegister)
                .subscribe { presenter.rx_onUpdate.onNext(ObserverState.ON_REGISTER) }

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

                            ObserverState.ON_REGISTER -> {
                                presenter.saveUserDetails()
                                showToast("Registered Successfully")
                                finish()
                            }

                            else -> "Do Nothing"
                        }
                    }
                })
    }

}
