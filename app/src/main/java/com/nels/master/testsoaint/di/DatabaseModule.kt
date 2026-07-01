package com.nels.master.testsoaint.di

import android.content.Context
import androidx.room.Room
import com.nels.master.testsoaint.data.local.dao.RegistroDao
import com.nels.master.testsoaint.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "test_soaint_db"
        ).build()
    }

    @Provides
    fun provideRegistroDao(database: AppDatabase): RegistroDao {
        return database.registroDao()
    }
}
