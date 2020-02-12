package info.goldenriver.monorail.core

import junit.framework.TestCase
import java.io.File

class ReaderTest : TestCase() {

    fun testGetReadDelegate() {}

    fun testSetReadDelegate() {}

    fun testGetContract() {}

    fun testSetContract() {}

    fun testGetResponse() {}

    fun testMonorailReader() {
        val content = Companion.getFileContent("quoteOfTheDay.json")
        val reader = Reader(content)
        kotlin.test.assertEquals(1, reader.contract?.interactions?.count())
        kotlin.test.assertEquals("http://api.theysaidso.com", reader.contract?.consumer?.baseUrl)
    }

    companion object {
        private fun getFileContent(fileName: String): String {
            val file = File(javaClass.classLoader.getResource(fileName).file)
            return file.readText()
        }
    }

}