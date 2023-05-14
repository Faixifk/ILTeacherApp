package com.example.intellilearnteacherapp

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.IOException
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
class ProgressRequestBody(
    private val inputStream: InputStream,
    private val contentLength: Long,
    private val contentType: MediaType?,
    private val listener: UploadProgressListener
) : RequestBody() {

    interface UploadProgressListener {
        fun onProgressUpdate(percentage: Int)
    }

    private var lastProgress = 0

    @Throws(IOException::class)
    override fun contentType(): MediaType? {
        return contentType
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return contentLength
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        var total: Long = 0
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var read: Int

        inputStream.use { input ->
            sink.buffer.use { output ->
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                    total += read.toLong()
                    val progress = ((total.toFloat() / contentLength) * 100).toInt()

                    if (progress > lastProgress + 1) {
                        listener.onProgressUpdate(progress)
                        lastProgress = progress
                    }
                }
            }
        }
    }

    companion object {
        const val DEFAULT_BUFFER_SIZE = 8192
        //const val DEFAULT_BUFFER_SIZE = 2048
    }
}
