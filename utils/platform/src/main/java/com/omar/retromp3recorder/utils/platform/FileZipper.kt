package com.omar.retromp3recorder.utils.platform

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipFile

object FileZipper {

    fun unzipFile(filePath: String, destination: String) {
        val input = ZipFile(filePath)
        val entries = input.entries()

        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()
            if (entry.isDirectory) {
                File(destination, entry.name)
            } else {
                BufferedOutputStream(FileOutputStream(File(destination, entry.name)))
                    .use { output ->
                        input.getInputStream(entry).use { data ->
                            output.write(data.readBytes())
                        }
                    }

            }
        }
    }
}
