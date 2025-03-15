package ui.command

import ui.PlaybackManager

class PlayPreviousTrackCommand(
    private val playbackManager: PlaybackManager,
) : Command {
    override fun execute() {
        playbackManager.playPreviousTrack()
    }
}
