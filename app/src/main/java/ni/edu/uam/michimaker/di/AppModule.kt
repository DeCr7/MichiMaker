package ni.edu.uam.michimaker.di

import android.content.Context
import ni.edu.uam.michimaker.database.AppDatabaseProvider
import ni.edu.uam.michimaker.repository.TransformacionRepository

object AppModule {

    fun provideRepository(
        context: Context
    ): TransformacionRepository {

        return TransformacionRepository(
            AppDatabaseProvider
                .obtener(context)
                .transformacionDao()
        )
    }
}