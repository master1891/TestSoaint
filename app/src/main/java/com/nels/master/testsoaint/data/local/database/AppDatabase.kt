package com.nels.master.testsoaint.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nels.master.testsoaint.data.local.dao.RegistroDao
import com.nels.master.testsoaint.data.local.entity.RegistroEntity

@Database(entities = [RegistroEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun registroDao(): RegistroDao
}
