package com.appworld.base.main

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import com.appworld.base.BaseApp
import com.appworld.base.R
import com.appworld.base.base.BaseActivity
import com.appworld.base.di.annotation.ViewObservables
import nucleus.factory.RequiresPresenter
import android.support.v7.widget.GridLayoutManager
import com.appworld.base.di.component.NetComponent
import com.appworld.base.util.ObserverState
import com.jakewharton.rxbinding.view.RxView
import rx.Observer


@RequiresPresenter(LoginViewPresenter::class)
class LandingActivity : BaseActivity<LoginViewPresenter>(), ViewObservables {
    private lateinit var buttonSwitch: Button
    private lateinit var recyclerView: RecyclerView

    private var recyclerAdapter: RecyclerAdapter? = null

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

        setContentView(R.layout.activity_landing)

        initView()

        registerViewObservables()
    }

    private fun initView() {
        buttonSwitch = findViewById(R.id.buttonSwitch)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerAdapter = RecyclerAdapter()

        val mLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.adapter = recyclerAdapter
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

    override fun registerViewObservables() {
        RxView.clicks(buttonSwitch)
                .subscribe { presenter.rx_onUpdate.onNext(ObserverState.ON_LOAD) }

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

                            ObserverState.ON_LOAD -> {
                                toggleView()
                            }

                            else -> "Do Nothing"
                        }
                    }
                })
    }

    private fun toggleView(){
        val isSwitched = recyclerAdapter?.toggleItemViewType()
        recyclerView.layoutManager = if (isSwitched!!) LinearLayoutManager(this) else GridLayoutManager(this, 2)
        recyclerAdapter?.notifyDataSetChanged()
    }
}
