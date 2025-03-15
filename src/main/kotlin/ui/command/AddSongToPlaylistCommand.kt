package ui.command

import ui.UserInputHandler

class AddSongToPlaylistCommand(
    private val userInputHandler: UserInputHandler,
) : Command {
    override fun execute() {
        userInputHandler.addSongToPlaylistPrompt()
    }
}
