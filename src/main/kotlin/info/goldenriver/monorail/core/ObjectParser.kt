package info.goldenriver.monorail.core

import java.io.IOException
import java.io.InputStream
import java.io.Reader
import java.lang.reflect.Type
import java.nio.charset.Charset

interface ObjectParser {
    @Throws(IOException::class)
    fun <T> parseAndClose(var1: InputStream?, var2: Charset?, var3: Class<T>?): T

    @Throws(IOException::class)
    fun parseAndClose(var1: InputStream?, var2: Charset?, var3: Type?): Any?

    @Throws(IOException::class)
    fun <T> parseAndClose(var1: Reader?, var2: Class<T>?): T

    @Throws(IOException::class)
    fun parseAndClose(var1: Reader?, var2: Type?): Any?
}