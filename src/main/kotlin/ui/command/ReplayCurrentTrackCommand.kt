package ui.command

import ui.PlaybackManager

class ReplayCurrentTrackCommand(
    private val playbackManager: PlaybackManager,
) : Command {
    override fun execute() {
        if (!playbackManager.replayCurrentTrack()) {
            println("Сначала выберите плейлист для воспроизведения")
        }
    }
}
