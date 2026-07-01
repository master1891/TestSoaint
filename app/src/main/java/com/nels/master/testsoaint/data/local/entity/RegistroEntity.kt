package com.nels.master.testsoaint.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registros")
data class RegistroEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val edad: Int,
    val nivelEstudios: String,
    val fechaCreacion: Long = System.currentTimeMillis(),
    val sincronizado: Boolean = false
)
