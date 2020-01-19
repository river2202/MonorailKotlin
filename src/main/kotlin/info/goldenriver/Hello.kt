package info.goldenriver


import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpRequest
import com.google.api.client.json.JsonObjectParser
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.Key
import com.google.api.client.http.MonorailNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport

open class Httpbin {

    data class Get (
        @Key var url: String? = null
    )

    fun get(): Get? {
        val requestFactory = MonorailNetHttpTransport(NetHttpTransport())
            .createRequestFactory{ request: HttpRequest ->
            request.headers.accept = "application/json"
            request.parser = JsonObjectParser(JacksonFactory())
        }

        val request = requestFactory.buildGetRequest(GenericUrl("https://httpbin.org/get"))
        return request.execute().parseAs(Get::class.java)
    }

}

fun main(args: Array<String>) {
    println("Hello, World")

}

