package com.demo.searchimagedemo.di

import androidx.lifecycle.ViewModel
import com.demo.searchimagedemo.di.ViewModelKey
import com.demo.searchimagedemo.viewmodel.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindViewModel(viewmodel: MainViewModel): ViewModel
}