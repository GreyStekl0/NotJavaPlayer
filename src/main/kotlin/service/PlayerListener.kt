package service

import javazoom.jlgui.basicplayer.BasicController
import javazoom.jlgui.basicplayer.BasicPlayerEvent
import javazoom.jlgui.basicplayer.BasicPlayerListener

class PlayerListener(
    private val onTrackEnded: () -> Unit,
) : BasicPlayerListener {
    override fun opened(
        stream: Any?,
        properties: Map<*, *>?,
    ) {
        //
    }

    override fun progress(
        bytesread: Int,
        microseconds: Long,
        pcmdata: ByteArray?,
        properties: Map<*, *>?,
    ) {
        //
    }

    override fun stateUpdated(event: BasicPlayerEvent?) {
        if (event?.code == BasicPlayerEvent.EOM) {
            onTrackEnded()
        }
    }

    override fun setController(controller: BasicController?) {
        //
    }
}
