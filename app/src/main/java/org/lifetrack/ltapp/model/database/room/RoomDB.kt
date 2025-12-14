package org.lifetrack.ltapp.model.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.lifetrack.ltapp.model.data.dao.ChatDao

@Database(entities = [MessageEntity::class], version = 1, exportSchema = false)
abstract class LTRoomDatabase: RoomDatabase(){
    abstract fun chatDao(): ChatDao

    companion object{
        @Volatile private var INSTANCE: LTRoomDatabase? = null

        fun getInstance(context: Context): LTRoomDatabase{
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    LTRoomDatabase::class.java,
                    "lifetrack_db"
                ).build()
                    .also { INSTANCE = it }
            }
        }
    }
}