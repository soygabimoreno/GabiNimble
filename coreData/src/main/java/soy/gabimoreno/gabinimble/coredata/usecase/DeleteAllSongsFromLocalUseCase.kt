package soy.gabimoreno.gabinimble.coredata.usecase

import soy.gabimoreno.gabinimble.coredata.repository.SongRepository

class DeleteAllSongsFromLocalUseCase(
    private val songRepository: SongRepository
) {
    suspend operator fun invoke() = songRepository.deleteAllSongsFromLocal()
}
