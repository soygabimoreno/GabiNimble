package soy.gabimoreno.gabinimble.coredb.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import soy.gabimoreno.gabinimble.coredb.model.DbSong

@Dao
interface SongDao {

    @Query("SELECT * FROM DbSong")
    fun getAll(): List<DbSong>

    @Query("SELECT * FROM DbSong WHERE id = :songId")
    fun findById(songId: Long): DbSong

    @Query("SELECT COUNT(id) FROM DbSong")
    fun songCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(dbSongs: List<DbSong>)

    @Query("DELETE FROM DbSong")
    fun deleteAll()
}
