package com.google.api.client.http

import com.google.api.client.http.LowLevelHttpRequest
import com.google.api.client.http.LowLevelHttpResponse
import com.google.api.client.http.MonorailNetHttpResponse
import com.google.api.client.util.Preconditions
import java.io.IOException
import java.net.HttpURLConnection


class MonorailNetHttpRequest(private val connection: HttpURLConnection) :
    LowLevelHttpRequest() {
    override fun addHeader(name: String, value: String) {
        connection.addRequestProperty(name, value)
    }

    override fun setTimeout(connectTimeout: Int, readTimeout: Int) {
        connection.readTimeout = readTimeout
        connection.connectTimeout = connectTimeout
    }

    @Throws(IOException::class)
    override fun execute(): LowLevelHttpResponse {
        val connection = connection
        if (streamingContent != null) {
            val contentType = contentType
            if (contentType != null) {
                addHeader("Content-Type", contentType)
            }
            val contentEncoding = contentEncoding
            if (contentEncoding != null) {
                addHeader("Content-Encoding", contentEncoding)
            }
            val contentLength = contentLength
            if (contentLength >= 0L) {
                addHeader("Content-Length", java.lang.Long.toString(contentLength))
            }
            val requestMethod = connection.requestMethod
            if ("POST" != requestMethod && "PUT" != requestMethod) {
                Preconditions.checkArgument(
                    contentLength == 0L,
                    "%s with non-zero content length is not supported",
                    *arrayOf<Any>(requestMethod)
                )
            } else {
                connection.doOutput = true
                if (contentLength in 0L..2147483647L) {
                    connection.setFixedLengthStreamingMode(contentLength.toInt())
                } else {
                    connection.setChunkedStreamingMode(0)
                }
                val out = connection.outputStream
                var threw = true
                threw = try {
                    streamingContent.writeTo(out)
                    false
                } finally {
                    try {
                        out.close()
                    } catch (var23: IOException) {
                        if (!threw) {
                            throw var23
                        }
                    }
                }
            }
        }
        var successfulConnection = false
        val var12: MonorailNetHttpResponse
        try {
            connection.connect()
            val response =
                MonorailNetHttpResponse(connection)
            successfulConnection = true
            var12 = response
        } finally {
            if (!successfulConnection) {
                connection.disconnect()
            }
        }
        return var12
    }

    init {
        connection.instanceFollowRedirects = false
    }
}