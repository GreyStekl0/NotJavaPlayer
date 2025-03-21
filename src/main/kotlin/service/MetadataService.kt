package service

import model.Track
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import java.io.File

class MetadataService : IMetadataService {
    override fun getMetadata(song: File): List<String> {
        val audioFile = AudioFileIO.read(song)
        val tag = audioFile.tag
        val title =
            if (tag.getFirst(FieldKey.TITLE).isEmpty()) {
                song.nameWithoutExtension
            } else {
                tag.getFirst(FieldKey.TITLE)
            }
        val artist =
            if (tag.getFirst(FieldKey.ARTIST).isEmpty()) {
                "Unknown"
            } else {
                tag.getFirst(FieldKey.ARTIST)
            }
        return listOf(title, artist)
    }

    override fun createTrack(file: File): Track {
        val mp3Data = getMetadata(file)
        return Track(file.path, mp3Data[0], mp3Data[1])
    }
}
