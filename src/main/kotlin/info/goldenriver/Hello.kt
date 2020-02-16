package info.goldenriver


import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpContent
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.MonorailNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.http.json.JsonHttpContent
import com.google.api.client.json.JsonObjectParser
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.Key
import java.util.function.Function

open class Httpbin {

    data class Get (
        @Key var url: String? = null
    )

    data class Post (
        @Key var url: String? = null
    )

    fun get(): Get? {
        val requestFactory = MonorailNetHttpTransport(NetHttpTransport())
            .createRequestFactory{ request: HttpRequest ->
            request.headers.accept = "application/json"
            request.parser = JsonObjectParser(JacksonFactory())
        }

        val request = requestFactory.buildGetRequest(GenericUrl("https://httpbin.org/get"))

        request.interceptor
        val response = request.execute()

        return response.parseAs(Get::class.java)
    }

    fun post(): Post? {
        val requestFactory = MonorailNetHttpTransport(NetHttpTransport())
            .createRequestFactory{ request: HttpRequest ->
                request.headers.accept = "application/json"
                request.parser = JsonObjectParser(JacksonFactory())
            }

        val content = Post("url")

        val httpContent = JsonHttpContent(
            JacksonFactory(),
            content
        )
        val request = requestFactory.buildPostRequest(GenericUrl("https://httpbin.org/post"), httpContent)

        val response = request.execute()

        return response.parseAs(Post::class.java)
    }
}

fun main(args: Array<String>) {
    println("Hello, World")
}

