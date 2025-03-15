package ui

class UserInputHandler(
    private val playlistHandler: PlaylistHandler,
    private val playbackManager: PlaybackManager,
) {
    fun createPlaylistPrompt() {
        println("Введите название плейлиста:")
        val name = readln()
        playlistHandler.createPlaylist(name)
        println("Плейлист '$name' создан")
    }

    fun removePlaylistPrompt() {
        println("Введите название плейлиста:")
        val name = readln()
        playlistHandler.removePlaylist(name)
        println("Плейлист '$name' удален")
    }

    fun showPlaylistPrompt() {
        println("Введите название плейлиста:")
        val name = readln()
        playlistHandler.showPlaylist(name)
    }

    fun playPlaylistPrompt() {
        println("Введите название плейлиста:")
        val name = readln()
        val playlist =
            playlistHandler.getPlaylist(name) ?: run {
                println("Плейлист '$name' не найден")
                return
            }
        playbackManager.playPlaylist(playlist.tracks)
    }

    fun addSongToPlaylistPrompt() {
        println("Введите название плейлиста:")
        val playlistName = readln()
        val playlist =
            playlistHandler.getPlaylist(playlistName) ?: run {
                println("Плейлист '$playlistName' не найден")
                return
            }

        println("Введите номер песни или название песни:")
        val trackInput = readln()
        val track = playlistHandler.getTrack(playlist, trackInput.toIntOrNull(), trackInput)
        if (track == null) {
            println("Песня не найдена")
            return
        }

        playlistHandler.addTrackToPlaylist(playlistName, track)
        println("Песня добавлена в плейлист '$playlistName'")
    }

    fun removeSongFromPlaylistPrompt() {
        println("Введите название плейлиста:")
        val playlistName = readln()
        val playlist =
            playlistHandler.getPlaylist(playlistName) ?: run {
                println("Плейлист '$playlistName' не найден")
                return
            }

        println("Введите номер песни или название песни:")
        val trackInput = readln()
        val track = playlistHandler.getTrack(playlist, trackInput.toIntOrNull(), trackInput)
        if (track == null) {
            println("Песня не найдена")
            return
        }

        playlistHandler.removeTrackFromPlaylist(playlistName, track)
        println("Песня удалена из плейлиста '$playlistName'")
    }
}
