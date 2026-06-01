package ni.edu.uam.michimaker.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        TransformacionEntity::class
    ],
    version = 1
)
abstract class MichiDatabase
    : RoomDatabase() {

    abstract fun transformacionDao():
            TransformacionDao
}