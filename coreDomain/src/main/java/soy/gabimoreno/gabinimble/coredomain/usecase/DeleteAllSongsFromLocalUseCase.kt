package soy.gabimoreno.gabinimble.coredomain.usecase

import soy.gabimoreno.gabinimble.coredomain.repository.SongRepository

class DeleteAllSongsFromLocalUseCase(
    private val songRepository: SongRepository
) {
    suspend operator fun invoke() = songRepository.deleteAllSongsFromLocal()
}
