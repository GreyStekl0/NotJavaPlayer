package service

import java.io.File

interface IDirectoryService {
    fun createPlayerFolder(directory: File): File

    fun findAllMp3Files(directory: File): List<File>

    fun getDefaultDirectory(): File
}
