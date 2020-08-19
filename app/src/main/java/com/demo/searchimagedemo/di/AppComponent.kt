package com.demo.searchimagedemo.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        SubcomponentModule::class,
        ViewModelBuilderModule::class
    ]
)
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): AppComponent
    }

    fun mainActivityComponent(): MainActivityComponent.Factory
}


@Module(
    subcomponents = [
        MainActivityComponent::class
    ]
)
interface SubcomponentModule