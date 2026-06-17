package ni.edu.uam.michimaker.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransformacionDao {

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertar(
        transformacion: TransformacionEntity
    )

    @Delete
    suspend fun eliminar(
        transformacion: TransformacionEntity
    )

    @Query("DELETE FROM transformaciones")
    suspend fun eliminarTodo()

    @Query("""
DELETE FROM transformaciones
WHERE usuarioId = :usuarioId
""")
    suspend fun eliminarPorUsuario(
        usuarioId: Int
    )

    @Query("SELECT * FROM transformaciones")
    fun obtenerTodas():
            Flow<List<TransformacionEntity>>

    @Query("""
    SELECT COUNT(*)
    FROM transformaciones
""")
    fun contarTransformaciones():
            Flow<Int>

    @Query("""
SELECT *
FROM transformaciones
WHERE usuarioId = :usuarioId
ORDER BY id DESC
""")
    fun obtenerPorUsuario(
        usuarioId: Int
    ): Flow<List<TransformacionEntity>>

    @Query("""
SELECT COUNT(*)
FROM transformaciones
WHERE usuarioId = :usuarioId
""")
    fun contarPorUsuario(
        usuarioId: Int
    ): Flow<Int>
}