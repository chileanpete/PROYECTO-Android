package com.example.proyecto_droid.data.model
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [Categoria::class, Lugar::class,Plato::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoriaDao(): CategoriaDao
    abstract fun lugarDao(): LugarDao
    abstract fun platoDao(): PlatoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "media_explorer_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}