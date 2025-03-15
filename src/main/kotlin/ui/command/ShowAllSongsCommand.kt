package ui.command

import ui.PlaylistDisplay

class ShowAllSongsCommand(
    private val playlistDisplay: PlaylistDisplay,
) : Command {
    override fun execute() {
        playlistDisplay.showAllSongs()
    }
}
