package com.example.proyecto_droid.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proyecto_droid.data.local.dao.PlatoFavoritoDao
import com.example.proyecto_droid.data.local.dao.TallerDao
import com.example.proyecto_droid.data.local.dao.UserDao
import com.example.proyecto_droid.data.local.entity.PlatoFavoritoEntity
import com.example.proyecto_droid.data.local.entity.TallerEntity
import com.example.proyecto_droid.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, TallerEntity::class, PlatoFavoritoEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tallerDao(): TallerDao
    abstract fun platofavoritoDao(): PlatoFavoritoDao
    abstract fun userDao(): UserDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vida_sana_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 