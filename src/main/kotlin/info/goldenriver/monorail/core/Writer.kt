package info.goldenriver.monorail.core

import com.google.api.client.http.HttpTransport
import java.io.DataInput
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Created by River H. on 10/06/18.
 */
class Writer {
    val logger: Logger = java.util.logging.Logger.getLogger(Writer::class.java.name)
//
//    @Throws(IOException::class)
//    fun logRequest(url: String, method: String, header: Map<String, String>, protocol: String, body: DataInput?) {
//
//        val requestBody = body
//        val hasRequestBody = requestBody != null
//
//        var requestStartMessage = "--> $method $url $protocol"
//        logger.log(Level.INFO, requestStartMessage)
//
//        if (hasRequestBody) {
//            // Request body headers are only present when installed as a network interceptor. Force
//            // them to be included (when available) so there values are known.
//            if (requestBody!!.contentType() != null) {
//                logger.log(Level.INFO,"Content-Type: " + requestBody!!.contentType())
//            }
//            if (requestBody!!.contentLength() != -1L) {
//                logger.log("Content-Length: " + requestBody!!.contentLength())
//            }
//        }
//
////        val headers = request.headers()
//        var i = 0
//        val count = headers.size()
//        while (i < count) {
//            val name = headers.name(i)
//            // Skip headers from the request body as they are explicitly logged above.
//            if (!"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(name, ignoreCase = true)) {
//                logger.log(name + ": " + headers.value(i))
//            }
//            i++
//        }
//
//        if (hasRequestBody.not()) {
//            logger.log("--> END " + request.method())
//        } else if (bodyEncoded(request.headers())) {
//            logger.log("--> END " + request.method() + " (encoded body omitted)")
//        } else {
//            val buffer = Buffer()
//            requestBody!!.writeTo(buffer)
//
//            var charset = UTF8
//            val contentType = requestBody!!.contentType()
//            if (contentType != null) {
//                charset = contentType.charset(UTF8)
//            }
//
//            logger.log("")
//            if (isPlaintext(buffer)) {
//                logger.log(buffer.readString(charset))
//                logger.log(
//                    "--> END " + request.method()
//                            + " (" + requestBody?.contentLength() + "-byte body)"
//                )
//            } else {
//                logger.log(
//                    "--> END " + request.method() + " (binary "
//                            + requestBody!!.contentLength() + "-byte body omitted)"
//                )
//            }
//        }
//    }
//
//    fun logResponse() {
//        val startNs = System.nanoTime()
//        val response: Response
//        try {
//            response = chain.proceed(request)
//        } catch (e: Exception) {
//            logger.log("<-- HTTP FAILED: " + e)
//            throw e
//        }
//
//        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
//
//        val responseBody = response.body()
//        val contentLength = responseBody?.contentLength()
//        logger.log("<-- " + response.code() + ' ' + response.message() + ' '
//                + response.request().url() + " (" + tookMs + "ms" + ')')
//
//        i = 0
//        while (i < count) {
//            logger.log(headers.name(i) + ": " + headers.value(i))
//            i++
//        }
//
//        if (!HttpHeaders.hasBody(response)) {
//            logger.log("<-- END HTTP")
//        } else if (bodyEncoded(response.headers())) {
//            logger.log("<-- END HTTP (encoded body omitted)")
//        } else {
//            val source = responseBody!!.source()
//            source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
//            val buffer = source.buffer()
//
//            var charset = UTF8
//            val contentType = responseBody.contentType()
//            if (contentType != null) {
//                charset = contentType.charset(UTF8)
//            }
//
//            if (!isPlaintext(buffer)) {
//                logger.log("")
//                logger.log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)")
//                return response
//            }
//
//            if (contentLength != 0L) {
//                logger.log("")
//                logger.log(buffer.clone().readString(charset))
//            }
//
//            logger.log("<-- END HTTP (" + buffer.size() + "-byte body)")
//        }
//
//
//        return response
//    }
//
//    private fun bodyEncoded(headers: Headers): Boolean {
//        val contentEncoding = headers.get("Content-Encoding")
//        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
//    }
//
//    companion object {
//
//        private val UTF8 = Charset.forName("UTF-8")
//
//        /**
//         * Returns true if the body in question probably contains human readable text. Uses a small sample
//         * of code points to detect unicode control characters commonly used in binary file signatures.
//         */
//        internal fun isPlaintext(buffer: Buffer): Boolean {
//            try {
//                val prefix = Buffer()
//                val byteCount = if (buffer.size() < 64) buffer.size() else 64
//                buffer.copyTo(prefix, 0, byteCount)
//                for (i in 0..15) {
//                    if (prefix.exhausted()) {
//                        break
//                    }
//                    val codePoint = prefix.readUtf8CodePoint()
//                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
//                        return false
//                    }
//                }
//                return true
//            } catch (e: EOFException) {
//                return false // Truncated UTF-8 sequence.
//            }
//
//        }
//    }
}