package com.google.api.client.http

import com.google.api.client.util.FieldInfo
import com.google.api.client.util.Preconditions
import com.google.api.client.util.Types
import java.io.IOException
import java.util.*

class MonorailNetHttpTransport (
    private val transport: HttpTransport
) :
    HttpTransport() {

    override fun supportsMethod(method: String): Boolean {
        return transport.supportsMethod(method)
    }

    @Throws(IOException::class)
    override fun buildRequest(method: String, url: String): LowLevelHttpRequest {
        return MonorailNetHttpRequest(method, url, transport.buildRequest(method, url))
    }

//    override fun buildRequest(): HttpRequest? {
//        return HttpRequest(this, null as String?)
////            .apply {
////            setResponseInterceptor {
//////                log(it.request)
//////                log(it)
////            }
//        }
//    }

    private fun log(request: HttpRequest) {
        println("-------------- Monorail - REQUEST  --------------")
        println("${request.requestMethod} ${request.url}")
        serializeHeaders(request.headers)
        println("-------------- Monorail - REQUEST - end  --------------")
    }

    private fun log(response: HttpResponse) {
        println("-------------- Monorail - RESPONSE  --------------")
//        println("${response.requestMethod} ${response.url}")
        serializeHeaders(response.headers)
        println("-------------- Monorail - RESPONSE -end  --------------")
    }


    @Throws(IOException::class)
    fun serializeHeaders(
        headers: HttpHeaders
    ): MutableMap<String, Any> {
        var headerMap: MutableMap<String, Any> = emptyMap<String, Any> ().toMutableMap()
        val headerNames: HashSet<String?> = HashSet<String?>()
        val `i$`: Iterator<*> = headers.entries.iterator()
        while (true) {
            while (true) {
                var name: String
                var value: Any
                do {
                    if (!`i$`.hasNext()) {
                        return headerMap
                    }
                    val headerEntry: Map.Entry<String, Any> =
                        `i$`.next() as Map.Entry<String, Any>
                    name = headerEntry.key
                    Preconditions.checkArgument(
                        headerNames.add(name),
                        "multiple headers of the same name (headers are case insensitive): %s",
                        *arrayOf<Any?>(name)
                    )
                    value = headerEntry.value
                } while (value == null)
                var displayName = name
                val fieldInfo = headers.classInfo.getFieldInfo(name)
                if (fieldInfo != null) {
                    displayName = fieldInfo.name
                }
                val valueClass: Class<out Any> = value.javaClass
                if (value !is Iterable<*> && !valueClass.isArray) {
                    headerMap[displayName] = value
                    print("$displayName ${toStringValue(value)}")
                } else {
                    val `i$`: Iterator<*> =
                        Types.iterableOf<Any>(value).iterator()
                    while (`i$`.hasNext()) {
                        val repeatedValue = `i$`.next()!!
                        headerMap[displayName] =  repeatedValue
                        println("$displayName: ${toStringValue(repeatedValue)}")
                    }
                }
            }
        }

        return headerMap
    }

    private fun toStringValue(headerValue: Any): String? {
        return if (headerValue is Enum<*>) FieldInfo.of(headerValue).name else headerValue.toString()
    }


}
