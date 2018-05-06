package com.appworld.base.di.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import javax.inject.Qualifier

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
annotation class ForApplication
