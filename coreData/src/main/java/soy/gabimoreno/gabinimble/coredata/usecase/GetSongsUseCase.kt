package soy.gabimoreno.gabinimble.coredata.usecase

import soy.gabimoreno.gabinimble.coredata.repository.SongRepository
import soy.gabimoreno.gabinimble.coredomain.Song

class GetSongsUseCase(
    private val songRepository: SongRepository
) {
    suspend operator fun invoke(): List<Song> = songRepository.getSongs()
}
