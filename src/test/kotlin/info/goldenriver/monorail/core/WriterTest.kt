package info.goldenriver.monorail.core

import info.goldenriver.Httpbin
import info.goldenriver.enableLogging
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals

class WriterTest : TestCase() {
    fun testWriteLog() {
        enableLogging()
        val httpbin = Httpbin()
        val httpbinGet = httpbin.get()
        assertEquals("https://httpbin.org/get", httpbinGet?.url)
    }

}