package com.nels.master.testsoaint.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.nels.master.testsoaint.data.local.entity.RegistroEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(registro: RegistroEntity): Long

    @Update
    suspend fun actualizar(registro: RegistroEntity)

    @Delete
    suspend fun eliminar(registro: RegistroEntity)

    @Query("DELETE FROM registros WHERE id = :id")
    suspend fun eliminarPorId(id: Long)

    @Query("SELECT * FROM registros ORDER BY fechaCreacion DESC")
    suspend fun obtenerTodos(): List<RegistroEntity>

    @Query("SELECT * FROM registros ORDER BY fechaCreacion DESC")
    fun obtenerTodosFlow(): Flow<List<RegistroEntity>>

    @Query("SELECT * FROM registros WHERE id = :id")
    suspend fun obtenerPorId(id: Long): RegistroEntity?

    @Query("UPDATE registros SET sincronizado = :sincronizado WHERE id = :id")
    suspend fun actualizarSincronizado(id: Long, sincronizado: Boolean)
}
