package ui.command

import ui.UserInputHandler

class PlayPlaylistCommand(
    private val userInputHandler: UserInputHandler,
) : Command {
    override fun execute() {
        userInputHandler.playPlaylistPrompt()
    }
}
