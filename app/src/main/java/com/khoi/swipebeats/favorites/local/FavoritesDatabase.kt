
package com.khoi.swipebeats.favorites.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteTrackEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FavoritesDatabase : RoomDatabase() {

    abstract fun favoriteTrackDao(): FavoriteTrackDao

    companion object {
        @Volatile
        private var INSTANCE: FavoritesDatabase? = null

        fun getInstance(context: Context): FavoritesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoritesDatabase::class.java,
                    "favorites_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}

