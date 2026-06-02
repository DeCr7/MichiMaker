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

    @Query("SELECT * FROM transformaciones")
    fun obtenerTodas():
            Flow<List<TransformacionEntity>>

    @Query("""
    SELECT COUNT(*)
    FROM transformaciones
""")
    fun contarTransformaciones():
            Flow<Int>
}