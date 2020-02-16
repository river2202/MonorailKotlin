package com.google.api.client.http

import java.io.FilterInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.util.*

internal class MonorailNetHttpResponse(private val lowLevelHttpResponse: LowLevelHttpResponse) :
    LowLevelHttpResponse() {

    override fun getStatusCode(): Int {
        return lowLevelHttpResponse.statusCode
    }

    @Throws(IOException::class)
    override fun getContent(): InputStream? {
        return lowLevelHttpResponse.content
    }

    @Throws(IOException::class)
    override fun getContentEncoding(): String? {
        return lowLevelHttpResponse.contentEncoding
    }

    @Throws(IOException::class)
    override fun getContentLength(): Long {
        return lowLevelHttpResponse.contentLength
    }

    @Throws(IOException::class)
    override fun getContentType(): String {
        return lowLevelHttpResponse.contentType
    }

    @Throws(IOException::class)
    override fun getReasonPhrase(): String {
        return lowLevelHttpResponse.reasonPhrase
    }

    @Throws(IOException::class)
    override fun getStatusLine(): String? {
        return lowLevelHttpResponse.statusLine
    }

    @Throws(IOException::class)
    override fun getHeaderCount(): Int {
        return lowLevelHttpResponse.headerCount
    }

    @Throws(IOException::class)
    override fun getHeaderName(index: Int): String {
        return lowLevelHttpResponse.getHeaderName(index)
    }

    @Throws(IOException::class)
    override fun getHeaderValue(index: Int): String {
        return lowLevelHttpResponse.getHeaderValue(index)
    }

    @Throws(IOException::class)
    override fun disconnect() {
        lowLevelHttpResponse.disconnect()
    }
}