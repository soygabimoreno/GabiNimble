package soy.gabimoreno.gabinimble.coredb.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import soy.gabimoreno.gabinimble.coredb.model.DbSong

@Database(
    entities = [DbSong::class],
    version = 2
)
abstract class SongDb : RoomDatabase() {

    companion object {
        fun build(context: Context) = Room.databaseBuilder(
            context,
            SongDb::class.java,
            "song-db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    abstract fun songDao(): SongDao
}
