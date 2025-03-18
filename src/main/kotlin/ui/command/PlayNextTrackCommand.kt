package ui.command

import ui.PlaybackManager

class PlayNextTrackCommand(
    private val playbackManager: PlaybackManager,
) : Command {
    override fun execute() {
        if (!playbackManager.playNextTrack()) {
            println("Сначала выберите плейлист для воспроизведения")
        }
    }
}
