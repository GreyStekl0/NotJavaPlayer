package ui

import model.Playlist
import model.Track

class UserInputHandler(
    private val playlistManager: PlaylistManager,
    private val playlistDisplay: PlaylistDisplay,
    private val playbackManager: PlaybackManager,
) {
    private fun promptForPlaylist(): Pair<String, Playlist>? {
        println("Выберите название плейлист:")
        playlistDisplay.showAllPlaylists()
        val playlistName = readln()
        val playlist = playlistManager.getPlaylist(playlistName)
        return if (playlist != null) {
            Pair(playlistName, playlist)
        } else {
            println("Плейлист '$playlistName' не найден")
            null
        }
    }

    private fun promptForTrack(playlist: Playlist): Track? {
        println("Введите номер песни или название песни:")
        playlistDisplay.showAllSongs()
        val trackInput = readln()
        val track = playlistManager.getTrack(playlist, trackInput.toIntOrNull(), trackInput)
        if (track == null) {
            println("Песня не найдена")
        }
        return track
    }

    fun createPlaylistPrompt() {
        println("Введите название плейлиста:")
        val name = readln()
        if (playlistManager.getPlaylist(name) != null) {
            println("Плейлист '$name' уже существует")
        } else {
            playlistManager.createPlaylist(name)
            println("Плейлист '$name' создан")
        }
    }

    fun removePlaylistPrompt() {
        println("Введите название плейлиста:")
        val name = readln()
        if (name == "All songs") {
            println("Нельзя удалить плейлист 'All songs'")
        } else if (playlistManager.removePlaylist(name)) {
            println("Плейлист '$name' удален")
        } else {
            println("Плейлист '$name' не найден")
        }
    }

    fun showPlaylistPrompt() {
        val (name, _) = promptForPlaylist() ?: return
        playlistDisplay.showPlaylist(name)
    }

    fun playPlaylistPrompt() {
        val (_, playlist) = promptForPlaylist() ?: return
        if (playlist.tracks.isEmpty()) {
            println("Плейлист пуст")
        } else {
            playbackManager.playPlaylist(playlist.tracks)
        }
    }

    fun addSongToPlaylistPrompt() {
        val playlistResult = promptForPlaylist()
        val allSongsPlaylist = playlistManager.getPlaylist("All songs")

        if (playlistResult != null && allSongsPlaylist != null) {
            val (playlistName, _) = playlistResult
            val track = promptForTrack(allSongsPlaylist)

            if (track != null) {
                val success = playlistManager.addTrackToPlaylist(playlistName, track)
                println(
                    if (success) {
                        "Песня добавлена в плейлист '$playlistName'"
                    } else {
                        "Песня уже есть в плейлисте '$playlistName'"
                    },
                )
            }
        }
    }

    fun removeSongFromPlaylistPrompt() {
        val (playlistName, playlist) = promptForPlaylist() ?: return
        val track = promptForTrack(playlist) ?: return

        val success = playlistManager.removeTrackFromPlaylist(playlistName, track)
        println(
            if (success) {
                "Песня удалена из плейлиста '$playlistName'"
            } else {
                "Песня не найдена в плейлисте '$playlistName'"
            },
        )
    }
}
