package service

import io.mockk.every
import io.mockk.mockk
import model.Playlist
import model.Track
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import repository.IPlaylistRepository
import repository.PlaylistRepository
import java.io.File
import kotlin.test.assertTrue

class PlaylistServiceTest {
    private lateinit var repository: IPlaylistRepository

    @BeforeEach
    fun setUp() {
        repository = mockk<PlaylistRepository>()
    }

    @Test
    fun `addTrackToPlaylist returns false if playlist is null`(
        @TempDir directory: File,
    ) {
        val service = PlaylistService(repository)
        val playlistName = "playlist"
        val track = Track("path", "title", "artist")

        every { service.getPlaylist(directory, playlistName) } returns null

        val result = service.addTrackToPlaylist(directory, playlistName, track)

        assertFalse(result)
    }

    @Test
    fun `addTrackToPlaylist returns false if track is already in playlist`(
        @TempDir directory: File,
    ) {
        val service = PlaylistService(repository)
        val playlistName = "playlist"
        val track = Track("path", "title", "artist")

        every { service.getPlaylist(directory, playlistName) } returns Playlist(playlistName, mutableListOf(track))

        val result = service.addTrackToPlaylist(directory, playlistName, track)

        assertFalse(result)
    }

    @Test
    fun `addTrackToPlaylist saves playlist with added track`(
        @TempDir directory: File,
    ) {
        val service = PlaylistService(repository)
        val playlistName = "playlist"
        val track = Track("path", "title", "artist")
        val playlist = Playlist(playlistName, mutableListOf())

        every { repository.getPlaylist(directory, playlistName) } returns playlist
        every { repository.savePlaylist(directory, any()) } returns Unit

        val result = service.addTrackToPlaylist(directory, playlistName, track)

        assertTrue(result)
        assertTrue(track in playlist.tracks)
    }

    @Test
    fun `removeTrackFromPlaylist returns false if playlist is null`(
        @TempDir directory: File,
    ) {
        val service = PlaylistService(repository)
        val playlistName = "playlist"
        val track = Track("path", "title", "artist")

        every { service.getPlaylist(directory, playlistName) } returns null

        val result = service.removeTrackFromPlaylist(directory, playlistName, track)

        assertFalse(result)
    }

    @Test
    fun `removeTrackFromPlaylist returns false if track is not in playlist`(
        @TempDir directory: File,
    ) {
        val service = PlaylistService(repository)
        val playlistName = "playlist"
        val track = Track("path", "title", "artist")

        every { service.getPlaylist(directory, playlistName) } returns Playlist(playlistName, mutableListOf())

        val result = service.removeTrackFromPlaylist(directory, playlistName, track)

        assertFalse(result)
    }

    @Test
    fun `removeTrackFromPlaylist saves playlist with removed track`(
        @TempDir directory: File,
    ) {
        val service = PlaylistService(repository)
        val playlistName = "playlist"
        val track = Track("path", "title", "artist")
        val playlist = Playlist(playlistName, mutableListOf(track))

        every { repository.getPlaylist(directory, playlistName) } returns playlist
        every { repository.savePlaylist(directory, any()) } returns Unit

        val result = service.removeTrackFromPlaylist(directory, playlistName, track)

        assertTrue(result)
        assertTrue(track !in playlist.tracks)
    }
}
