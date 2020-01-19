package com.google.api.client.http

import java.io.FilterInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.util.*

internal class MonorailNetHttpResponse(private val connection: HttpURLConnection) :
    LowLevelHttpResponse() {
    private val responseCode: Int
    private var responseMessage: String
    private val headerNames = ArrayList<String?>()
    private val headerValues = ArrayList<String?>()

    override fun getStatusCode(): Int {
        return responseCode
    }

    @Throws(IOException::class)
    override fun getContent(): InputStream? {
        var `in`: InputStream? = null
        `in` = try {
            connection.inputStream
        } catch (var3: IOException) {
            connection.errorStream
        }
        return `in`?.let { SizeValidatingInputStream(it) }
    }

    override fun getContentEncoding(): String {
        return connection.contentEncoding
    }

    override fun getContentLength(): Long {
        val string = connection.getHeaderField("Content-Length")
        return string?.toLong() ?: -1L
    }

    override fun getContentType(): String {
        return connection.getHeaderField("Content-Type")
    }

    override fun getReasonPhrase(): String {
        return responseMessage
    }

    override fun getStatusLine(): String? {
        val result = connection.getHeaderField(0)
        return if (result != null && result.startsWith("HTTP/1.")) result else null
    }

    override fun getHeaderCount(): Int {
        return headerNames.size
    }

    override fun getHeaderName(index: Int): String {
        return headerNames[index] as String
    }

    override fun getHeaderValue(index: Int): String {
        return headerValues[index] as String
    }

    override fun disconnect() {
        connection.disconnect()
    }

    private inner class SizeValidatingInputStream(`in`: InputStream?) :
        FilterInputStream(`in`) {
        private var bytesRead = 0L
        @Throws(IOException::class)
        override fun read(b: ByteArray, off: Int, len: Int): Int {
            val n = `in`.read(b, off, len)
            if (n == -1) {
                throwIfFalseEOF()
            } else {
                bytesRead += n.toLong()
            }
            return n
        }

        @Throws(IOException::class)
        override fun read(): Int {
            val n = `in`.read()
            if (n == -1) {
                throwIfFalseEOF()
            } else {
                ++bytesRead
            }
            return n
        }

        @Throws(IOException::class)
        private fun throwIfFalseEOF() {
            val contentLength = this@MonorailNetHttpResponse.contentLength
            if (contentLength != -1L) {
                if (bytesRead != 0L && bytesRead < contentLength) {
                    val var3 = bytesRead
                    throw IOException(
                        StringBuilder(102).append("Connection closed prematurely: bytesRead = ").append(
                            var3
                        ).append(", Content-Length = ").append(contentLength).toString()
                    )
                }
            }
        }
    }

    init {
        val responseCode = connection.responseCode
        this.responseCode = if (responseCode == -1) 0 else responseCode
        this.responseMessage = connection.responseMessage
        val headerNames: MutableList<String?> = headerNames
        val headerValues: MutableList<String?> = headerValues
        val `i$` = connection.headerFields.entries.iterator()

        run {
            while (true) {
                var entry: Map.Entry<*, *>
                var key: String?
                do {
                    if (!`i$`.hasNext()) {
                        return@run
                    }
                    entry = `i$`.next() as Map.Entry<*, *>
                    key = entry.key as String?
                } while (key == null)
                val `i$` = (entry.value as List<*>).iterator()
                while (`i$`.hasNext()) {
                    val value = `i$`.next() as String?
                    if (value != null) {
                        headerNames.add(key)
                        headerValues.add(value)
                    }
                }
            }
        }
    }
}