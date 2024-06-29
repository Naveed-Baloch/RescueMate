package com.rescuemate.app.di

import android.app.Application
import android.content.Context
import com.rescuemate.app.sharedpref.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideSharedPref(context: Context): UserPreferences {
        return UserPreferences(context)
    }

}