package com.google.api.client.http


import com.google.api.client.http.javanet.ConnectionFactory
import com.google.api.client.http.javanet.DefaultConnectionFactory
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.util.Beta
import com.google.api.client.util.SecurityUtils
import com.google.api.client.util.SslUtils
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.Proxy
import java.security.GeneralSecurityException
import java.security.KeyStore
import java.util.*
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory

class MonorailNetHttpTransport (
    private val transport: HttpTransport
) :
    HttpTransport() {

    override fun supportsMethod(method: String): Boolean {
        return transport.supportsMethod(method)
    }

    @Throws(IOException::class)
    override fun buildRequest(method: String, url: String): LowLevelHttpRequest {
        return transport.buildRequest(method, url)
    }
}
