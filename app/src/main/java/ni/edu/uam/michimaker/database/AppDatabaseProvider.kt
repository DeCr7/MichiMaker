package ni.edu.uam.michimaker.database

import android.content.Context
import androidx.room.Room

object AppDatabaseProvider {

    private var db: MichiDatabase? = null

    fun obtener(context: Context): MichiDatabase {

        return db ?: Room.databaseBuilder(
            context,
            MichiDatabase::class.java,
            "michimaker.db"
        ).build().also {
            db = it
        }
    }
}