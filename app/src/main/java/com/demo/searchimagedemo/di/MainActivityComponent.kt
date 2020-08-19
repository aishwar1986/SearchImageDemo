package com.demo.searchimagedemo.di

import com.demo.searchimagedemo.activity.MainActivity
import dagger.Subcomponent
import javax.inject.Scope

@ActivityScope
@Subcomponent(modules = [MainModule::class])
interface MainActivityComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MainActivityComponent
    }

    fun inject(activity: MainActivity)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope