package ui.command

import ui.UserInputHandler

class RemoveSongFromPlaylistCommand(
    private val userInputHandler: UserInputHandler,
) : Command {
    override fun execute() {
        userInputHandler.removeSongFromPlaylistPrompt()
    }
}
