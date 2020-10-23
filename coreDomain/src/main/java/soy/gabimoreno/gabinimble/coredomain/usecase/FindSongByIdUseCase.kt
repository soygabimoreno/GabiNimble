package soy.gabimoreno.gabinimble.coredomain.usecase

import soy.gabimoreno.gabinimble.coredomain.Song
import soy.gabimoreno.gabinimble.coredomain.repository.SongRepository

class FindSongByIdUseCase(
    private val songRepository: SongRepository
) {
    suspend operator fun invoke(filename: String, songId: Long): Song = songRepository.findById(filename, songId)
}
