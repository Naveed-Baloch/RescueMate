package com.rescuemate.app.di

import com.bkcoding.garagegurufyp_user.repository.fcm.FcmRepositoryImpl
import com.rescuemate.app.repository.auth.AuthRepository
import com.rescuemate.app.repository.auth.AuthRepositoryImpl
import com.rescuemate.app.repository.fcm.FcmRepository
import com.rescuemate.app.repository.user.UserRepository
import com.rescuemate.app.repository.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun providesFirebaseAuthRepository(repo: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun providesUserRepository(repo: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun providesFcmRepository(repo: FcmRepositoryImpl): FcmRepository

}