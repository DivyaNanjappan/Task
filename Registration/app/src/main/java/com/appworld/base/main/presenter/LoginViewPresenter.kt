package com.appworld.base.main

import android.view.View
import com.appworld.base.db.UserProfile
import com.appworld.base.util.Constants
import com.appworld.base.util.ObserverState
import com.flocks.address.user.cache.DataManager
import io.realm.Realm
import nucleus.presenter.RxPresenter
import rx.Observable
import rx.Observer
import rx.functions.Func2
import rx.functions.Func4
import rx.subjects.BehaviorSubject
import javax.inject.Inject


class LoginViewPresenter : RxPresenter<View>() {

    @Inject
    lateinit var dataManager: DataManager

    var rx_onUpdate = BehaviorSubject.create<ObserverState>()
    var rx_onError = BehaviorSubject.create<Exception>()

    var validateUserInputs: Observable<Boolean>? = null

    private var userProfile: UserProfile = UserProfile()

    private var checkUserName = ""
    private var checkPassword = ""

    //Thready specific
    private var realm: Realm? = null

    fun setupRealm() {
        realm = Realm.getDefaultInstance()
    }

    fun closeRealm() {
        if (realm != null) {
            if (!realm!!.isClosed) {
                realm!!.close()
            }
        }
    }

    fun attachObservables(userName: Observable<CharSequence>,
                          password: Observable<CharSequence>) {
        validateUserInputs = Observable.combineLatest(userName, password, Func2<CharSequence, CharSequence, Boolean> { userName, password ->
            if (userName != null && userName.length < Constants.UserInput.NOT_A_ZERO_INPUT) {
                return@Func2 false
            }
            !(password != null && password.length < Constants.UserInput.NOT_A_ZERO_INPUT)
        }).asObservable()


        userName.subscribe(object : Observer<CharSequence> {
            override fun onCompleted() {
                //Do nothing
            }

            override fun onError(e: Throwable) {
                //Do nothing
            }

            override fun onNext(charSequence: CharSequence?) {
                checkUserName = charSequence.toString()
            }
        })

        password.subscribe(object : Observer<CharSequence> {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(charSequence: CharSequence?) {
                checkPassword = charSequence.toString()
            }
        })
    }

    fun attachObservables(userName: Observable<CharSequence>,
                          password: Observable<CharSequence>,
                          email: Observable<CharSequence>,
                          mobileNo: Observable<CharSequence>) {

        validateUserInputs = Observable.combineLatest(userName, password, email, mobileNo,
                Func4<CharSequence, CharSequence, CharSequence, CharSequence, Boolean>
                { userName, password, email, mobileNo ->
                    if (userName != null && userName.length <= Constants.UserInput.NOT_A_ZERO_INPUT) {
                        return@Func4 false
                    }
                    if (password != null && password.length <= Constants.UserInput.NOT_A_ZERO_INPUT) {
                        return@Func4 false
                    }
                    if (email != null && email.length <= Constants.UserInput.NOT_A_ZERO_INPUT) {
                        return@Func4 false
                    }

                    !(mobileNo != null && mobileNo.length <= Constants.UserInput.MOBILE_NO_MIN)
                })
                .asObservable()

        userName.subscribe(object : Observer<CharSequence> {
            override fun onCompleted() {
                //Do nothing
            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(response: CharSequence) {
                userProfile.userName = response.toString()
            }
        })

        password.subscribe(object : Observer<CharSequence> {
            override fun onCompleted() {
                //Do nothing
            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(response: CharSequence) {
                userProfile.password = response.toString()
            }
        })

        email.subscribe(object : Observer<CharSequence> {
            override fun onCompleted() {
                //Do nothing
            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(response: CharSequence) {
                userProfile.email = response.toString()
            }
        })

        mobileNo.subscribe(object : Observer<CharSequence> {
            override fun onCompleted() {
                //Do nothing
            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(response: CharSequence) {
                userProfile.mobileNumber = response.toString()
            }
        })
    }


    fun isValidUser(): Boolean {
        return dataManager.verifyUserLogin(checkUserName, checkPassword)
    }

    fun saveUserDetails() {
        if(userProfile != null){
            dataManager.persistUserProfile(userProfile)
        }
    }
}
