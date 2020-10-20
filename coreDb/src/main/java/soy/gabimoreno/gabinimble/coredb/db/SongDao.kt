package soy.gabimoreno.gabinimble.coredb.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import soy.gabimoreno.gabinimble.coredb.model.DbSong

@Dao
interface SongDao {

    @Query("SELECT * FROM DbSong WHERE filename = :filename")
    fun getAll(filename: String): List<DbSong>

    @Query("SELECT * FROM DbSong WHERE filename = :filename AND id = :songId")
    fun findById(filename: String, songId: Long): DbSong

    @Query("SELECT COUNT(id) FROM DbSong WHERE filename = :filename")
    fun songCount(filename: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(dbSongs: List<DbSong>)

    @Query("DELETE FROM DbSong")
    fun deleteAll()
}
