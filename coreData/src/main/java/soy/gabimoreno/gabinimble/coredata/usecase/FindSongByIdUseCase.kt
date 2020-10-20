package soy.gabimoreno.gabinimble.coredata.usecase

import soy.gabimoreno.gabinimble.coredata.repository.SongRepository
import soy.gabimoreno.gabinimble.coredomain.Song

class FindSongByIdUseCase(
    private val songRepository: SongRepository
) {
    suspend operator fun invoke(filename: String, songId: Long): Song = songRepository.findById(filename, songId)
}
