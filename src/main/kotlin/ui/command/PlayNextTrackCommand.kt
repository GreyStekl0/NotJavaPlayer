package ui.command

import ui.PlaybackManager

class PlayNextTrackCommand(
    private val playbackManager: PlaybackManager,
) : Command {
    override fun execute() {
        playbackManager.playNextTrack()
    }
}
