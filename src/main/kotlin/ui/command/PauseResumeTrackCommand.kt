package ui.command

import service.PlayerService

class PauseResumeTrackCommand(
    private val playerService: PlayerService,
) : Command {
    override fun execute() {
        playerService.pauseAndResumeTrack()
    }
}
