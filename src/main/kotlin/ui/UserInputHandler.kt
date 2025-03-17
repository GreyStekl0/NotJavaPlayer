package ui

import model.Playlist
import model.Track

class UserInputHandler(
    private val playlistManager: PlaylistManager,
    private val playlistDisplay: PlaylistDisplay,
    private val playbackManager: PlaybackManager,
    private val reader: () -> String = ::readln,
    private val writer: (String) -> Unit = ::println,
) {
    private fun promptForPlaylist(): Pair<String, Playlist>? {
        writer("Выберите название плейлист:")
        playlistDisplay.showAllPlaylists()
        val playlistName = reader()
        val playlist = playlistManager.getPlaylist(playlistName)
        return if (playlist != null) {
            Pair(playlistName, playlist)
        } else {
            writer("Плейлист '$playlistName' не найден")
            null
        }
    }

    private fun promptForTrack(playlist: Playlist): Track? {
        writer("Введите номер песни или название песни:")
        playlistDisplay.showAllSongs()
        val trackInput = reader()
        val track = playlistManager.getTrack(playlist, trackInput.toIntOrNull(), trackInput)
        if (track == null) {
            writer("Песня не найдена")
        }
        return track
    }

    private fun promptForTrackFromPlaylist(
        playlist: Playlist,
        playlistName: String,
    ): Track? {
        writer("Введите номер песни или название песни из плейлиста '$playlistName':")
        playlistDisplay.showPlaylist(playlistName)
        val trackInput = reader()
        val track = playlistManager.getTrack(playlist, trackInput.toIntOrNull(), trackInput)
        if (track == null) {
            writer("Песня не найдена")
        }
        return track
    }

    fun createPlaylistPrompt() {
        writer("Введите название плейлиста:")
        val name = reader()
        if (playlistManager.getPlaylist(name) != null) {
            writer("Плейлист '$name' уже существует")
        } else {
            playlistManager.createPlaylist(name)
            writer("Плейлист '$name' создан")
        }
    }

    fun removePlaylistPrompt() {
        writer("Введите название плейлиста:")
        val name = reader()
        if (name == "All songs") {
            writer("Плейлист 'All songs' не может быть удален")
        } else if (playlistManager.removePlaylist(name)) {
            writer("Плейлист '$name' удален")
        } else {
            writer("Плейлист '$name' не найден")
        }
    }

    fun showPlaylistPrompt() {
        val (name, _) = promptForPlaylist() ?: return
        playlistDisplay.showPlaylist(name)
    }

    fun playPlaylistPrompt() {
        val (_, playlist) = promptForPlaylist() ?: return
        if (playlist.tracks.isEmpty()) {
            writer("Плейлист пуст")
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
                writer(
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
        val playlistResult = promptForPlaylist()

        if (playlistResult != null) {
            val (playlistName, playlist) = playlistResult

            if (playlist.tracks.isNotEmpty()) {
                val track = promptForTrackFromPlaylist(playlist, playlistName)

                if (track != null) {
                    val success = playlistManager.removeTrackFromPlaylist(playlistName, track)
                    writer(
                        if (success) {
                            "Песня удалена из плейлиста '$playlistName'"
                        } else {
                            "Песня не найдена в плейлисте '$playlistName'"
                        },
                    )
                }
            } else {
                writer("Плейлист пуст")
            }
        }
    }
}
