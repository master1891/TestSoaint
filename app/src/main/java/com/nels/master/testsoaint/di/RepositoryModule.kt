package com.nels.master.testsoaint.di

import com.nels.master.testsoaint.data.repository.AuthRepositoryImpl
import com.nels.master.testsoaint.data.repository.JwtDecoderImpl
import com.nels.master.testsoaint.data.repository.RegistroRepositoryImpl
import com.nels.master.testsoaint.domain.repository.AuthRepository
import com.nels.master.testsoaint.domain.repository.JwtDecoder
import com.nels.master.testsoaint.domain.repository.RegistroRepository
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
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindRegistroRepository(impl: RegistroRepositoryImpl): RegistroRepository

    @Binds
    @Singleton
    abstract fun bindJwtDecoder(impl: JwtDecoderImpl): JwtDecoder
}