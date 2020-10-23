package soy.gabimoreno.gabinimble.coredomain.usecase

import soy.gabimoreno.gabinimble.coredomain.Song
import soy.gabimoreno.gabinimble.coredomain.repository.SongRepository

class GetSongsUseCase(
    private val songRepository: SongRepository
) {
    suspend operator fun invoke(filename: String): List<Song> = songRepository.getSongs(filename)
}
