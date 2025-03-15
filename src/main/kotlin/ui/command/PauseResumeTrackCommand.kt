package ui.command

import service.IPlayerService

class PauseResumeTrackCommand(
    private val playerService: IPlayerService,
) : Command {
    override fun execute() {
        playerService.pauseAndResumeTrack()
    }
}
