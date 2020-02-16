package com.google.api.client.http

import com.google.api.client.util.LoggingByteArrayOutputStream
import com.google.api.client.util.Preconditions
import com.google.api.client.util.StreamingContent
import java.io.ByteArrayOutputStream
import java.io.FilterOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.NumberFormat
import java.util.logging.Level
import java.util.logging.Logger

class MonorailNetHttpRequest(
    private val method: String,
    private val url: String,
    private val lowLevelHttpRequest: LowLevelHttpRequest
) : LowLevelHttpRequest() {

    private var headers = mutableMapOf<String, String>()

    override fun addHeader(name: String, value: String) {
        lowLevelHttpRequest.addHeader(name, value)
        headers[name] = value
    }

    override fun setTimeout(connectTimeout: Int, readTimeout: Int) {
        lowLevelHttpRequest.setTimeout(connectTimeout, readTimeout)
    }

    private val contentLoggingLimit = 16384
    @Throws(IOException::class)
    override fun execute(): LowLevelHttpResponse {
        log()
        lowLevelHttpRequest.contentLength = contentLength
        lowLevelHttpRequest.contentEncoding = contentEncoding
        lowLevelHttpRequest.contentType = contentType
        lowLevelHttpRequest.streamingContent = MonorailLoggingStreamingContent(
                streamingContent as StreamingContent,
                HttpTransport.LOGGER,
                Level.CONFIG,
                this.contentLoggingLimit
            )
        return MonorailNetHttpResponse(lowLevelHttpRequest.execute())
    }

    private fun log() {
        println("-------------- Monorail - REQUEST  --------------")
        println("$method $url")
        headers.forEach {
            println("${it.key}: ${it.value}")
        }
        if (contentEncoding != null) {
            println("contentEncoding: $contentEncoding")
        }

        if (contentType != null) {
            println("contentType: $contentType")
        }
        if (contentLength > 0) {
            println("contentLength: $contentLength")
        }

        println("-------------- Monorail - REQUEST - end  --------------")
    }

}


class MonorailLoggingStreamingContent(
    private val content: StreamingContent,
    private val logger: Logger,
    private val loggingLevel: Level,
    private val contentLoggingLimit: Int
) :
    StreamingContent {
    @Throws(IOException::class)
    override fun writeTo(out: OutputStream) {
        val loggableOutputStream =
            MonorailLoggingOutputStream(out, logger, loggingLevel, contentLoggingLimit)
        try {
            content.writeTo(loggableOutputStream)
        } finally {
            loggableOutputStream.logStream.close()
        }
        out.flush()
    }

}


class MonorailLoggingOutputStream(
    outputStream: OutputStream?,
    logger: Logger?,
    loggingLevel: Level?,
    contentLoggingLimit: Int
) :
    FilterOutputStream(outputStream) {
    val logStream: MonorailLoggingByteArrayOutputStream

    @Throws(IOException::class)
    override fun write(b: Int) {
        out.write(b)
        logStream.write(b)
    }

    @Throws(IOException::class)
    override fun write(b: ByteArray, off: Int, len: Int) {
        out.write(b, off, len)
        logStream.write(b, off, len)
    }

    @Throws(IOException::class)
    override fun close() {
        logStream.close()
        super.close()
    }

    init {
        logStream = MonorailLoggingByteArrayOutputStream(logger, loggingLevel, contentLoggingLimit)
    }
}


class MonorailLoggingByteArrayOutputStream(
    logger: Logger?,
    loggingLevel: Level?,
    maximumBytesToLog: Int
) :
    ByteArrayOutputStream() {
    @get:Synchronized
    var bytesWritten = 0
        private set
    val maximumBytesToLog: Int
    private var closed = false
    private val loggingLevel: Level
    private val logger: Logger
    @Synchronized
    override fun write(b: Int) {
        Preconditions.checkArgument(!closed)
        ++bytesWritten
        if (count < maximumBytesToLog) {
            super.write(b)
        }
    }

    @Synchronized
    override fun write(b: ByteArray, off: Int, len: Int) {
        var len = len
        Preconditions.checkArgument(!closed)
        bytesWritten += len
        if (count < maximumBytesToLog) {
            val end = count + len
            if (end > maximumBytesToLog) {
                len += maximumBytesToLog - end
            }
            super.write(b, off, len)
        }
    }

    @Synchronized
    @Throws(IOException::class)
    override fun close() {
        if (!closed) {
            if (bytesWritten != 0) {
                val buf = StringBuilder().append("Monorail Total: ")
                appendBytes(buf, bytesWritten)
                if (count != 0 && count < bytesWritten) {
                    buf.append(" (logging first ")
                    appendBytes(buf, count)
                    buf.append(")")
                }
                logger.config(buf.toString())
                if (count != 0) {
                    logger.log(
                        loggingLevel,
                        this.toString("UTF-8").replace("[\\x00-\\x09\\x0B\\x0C\\x0E-\\x1F\\x7F]".toRegex(), " ")
                    )
                }
            }
            closed = true
        }
    }

    private fun appendBytes(buf: StringBuilder, x: Int) {
        if (x == 1) {
            buf.append("1 byte")
        } else {
            buf.append(NumberFormat.getInstance().format(x.toLong())).append(" bytes")
        }
    }

    init {
        this.logger =
            Preconditions.checkNotNull(logger) as Logger
        this.loggingLevel =
            Preconditions.checkNotNull(loggingLevel) as Level
        Preconditions.checkArgument(maximumBytesToLog >= 0)
        this.maximumBytesToLog = maximumBytesToLog
    }
}