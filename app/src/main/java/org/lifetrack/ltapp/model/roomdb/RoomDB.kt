package org.lifetrack.ltapp.model.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import org.lifetrack.ltapp.model.data.dao.ChatDao

@Database(entities = [MessageEntity::class], version = 1, exportSchema = false)
abstract class LTRoomDatabase: RoomDatabase(){
    abstract fun chatDao(): ChatDao

}