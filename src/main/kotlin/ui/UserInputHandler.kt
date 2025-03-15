package ui

import model.Playlist
import model.Track

class UserInputHandler(
    private val playlistHandler: PlaylistHandler,
    private val playbackManager: PlaybackManager,
) {
    private fun promptForPlaylist(): Pair<String, Playlist>? {
        println("Введите название плейлиста:")
        val playlistName = readln()
        val playlist = playlistHandler.getPlaylist(playlistName)
        return if (playlist != null) {
            Pair(playlistName, playlist)
        } else {
            println("Плейлист '$playlistName' не найден")
            null
        }
    }

    private fun promptForTrack(playlist: Playlist): Track? {
        println("Введите номер песни или название песни:")
        val trackInput = readln()
        val track = playlistHandler.getTrack(playlist, trackInput.toIntOrNull(), trackInput)
        if (track == null) {
            println("Песня не найдена")
        }
        return track
    }

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
        val (name, _) = promptForPlaylist() ?: return
        playlistHandler.showPlaylist(name)
    }

    fun playPlaylistPrompt() {
        val (_, playlist) = promptForPlaylist() ?: return
        playbackManager.playPlaylist(playlist.tracks)
    }

    fun addSongToPlaylistPrompt() {
        val (playlistName, playlist) = promptForPlaylist() ?: return
        val track = promptForTrack(playlist) ?: return

        playlistHandler.addTrackToPlaylist(playlistName, track)
        println("Песня добавлена в плейлист '$playlistName'")
    }

    fun removeSongFromPlaylistPrompt() {
        val (playlistName, playlist) = promptForPlaylist() ?: return
        val track = promptForTrack(playlist) ?: return

        playlistHandler.removeTrackFromPlaylist(playlistName, track)
        println("Песня удалена из плейлиста '$playlistName'")
    }
}
