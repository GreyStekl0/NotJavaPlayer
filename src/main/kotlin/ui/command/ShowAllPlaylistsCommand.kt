package ui.command

import ui.PlaylistDisplay

class ShowAllPlaylistsCommand(
    private val playlistDisplay: PlaylistDisplay,
) : Command {
    override fun execute() {
        playlistDisplay.showAllPlaylists()
    }
}
