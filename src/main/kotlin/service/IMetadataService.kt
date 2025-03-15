package service

import model.Track
import java.io.File

interface IMetadataService {
    fun getMetadata(song: File): List<String>

    fun createTrack(file: File): Track
}
