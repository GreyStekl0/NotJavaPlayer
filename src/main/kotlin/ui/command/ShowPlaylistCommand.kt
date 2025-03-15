package ui.command

import ui.UserInputHandler

class ShowPlaylistCommand(
    private val userInputHandler: UserInputHandler,
) : Command {
    override fun execute() {
        userInputHandler.showPlaylistPrompt()
    }
}
