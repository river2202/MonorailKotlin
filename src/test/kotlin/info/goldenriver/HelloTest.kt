package info.goldenriver

import com.google.api.client.http.HttpTransport
import info.goldenriver.monorail.core.Reader
import org.junit.Test
import java.io.File
import java.util.logging.Handler
import java.util.logging.Level
import java.util.logging.LogRecord
import java.util.logging.Logger
import kotlin.test.assertEquals


fun enableLogging() {
    val logger: Logger = Logger.getLogger(HttpTransport::class.java.name)
    logger.level = Level.CONFIG
    logger.addHandler(object : Handler() {
        @Throws(SecurityException::class)
        override fun close() {
        }

        override fun flush() {}
        override fun publish(record: LogRecord) { // Default ConsoleHandler will print >= INFO to System.err.
            if (record.level.intValue() < Level.INFO.intValue()) {
                println(record.message)
            }
        }
    })
}


class HelloTest {

    fun getFileContent(fileName: String): String {
        val file = File(javaClass.classLoader.getResource(fileName).file)
        val content =  file.readText()
        return content
    }

    @Test
    fun testGetRequest() {
        enableLogging()
        val httpbin = Httpbin()
        val httpbinGet = httpbin.get()

        assertEquals("https://httpbin.org/get", httpbinGet?.url)
    }

    @Test
    fun testMonorailReader() {
        val content = getFileContent("quoteOfTheDay.json")
        val reader = Reader(content)
        assertEquals(
            1,
            actual = reader.contract?.interactions?.count()
        )
    }

}
