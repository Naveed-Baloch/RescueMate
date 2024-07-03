package com.rescuemate.app.di

import com.rescuemate.app.repository.ambulance.AmbulanceRepository
import com.rescuemate.app.repository.ambulance.AmbulanceRepositoryImpl
import com.rescuemate.app.repository.fcm.FcmRepositoryImpl
import com.rescuemate.app.repository.auth.AuthRepository
import com.rescuemate.app.repository.auth.AuthRepositoryImpl
import com.rescuemate.app.repository.blood.BloodRepository
import com.rescuemate.app.repository.blood.BloodRepositoryImpl
import com.rescuemate.app.repository.fcm.FcmRepository
import com.rescuemate.app.repository.laboratory.LaboratoryRepository
import com.rescuemate.app.repository.laboratory.LaboratoryRepositoryImpl
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

    @Binds
    @Singleton
    abstract fun providesBloodRepository(repo: BloodRepositoryImpl): BloodRepository

    @Binds
    @Singleton
    abstract fun providesLaboratoryRepository(repo: LaboratoryRepositoryImpl): LaboratoryRepository

    @Binds
    @Singleton
    abstract fun providesAmbulanceRepository(repo: AmbulanceRepositoryImpl): AmbulanceRepository
}