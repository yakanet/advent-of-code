package common

import java.io.File

fun String.getText(mockIndex: Int = 0): String {
    return getFile(mockIndex).readText()
}

fun String.getLines(mockIndex: Int = 0): List<String> {
    return getFile(mockIndex).readLines()
}

fun String.getLinesInt(mockIndex: Int = 0): List<Int> {
    return getLines(mockIndex).map { it.toInt() }
}

fun String.getLinesLong(mockIndex: Int = 0): List<Long> {
    return getLines(mockIndex).map { it.toLong() }
}

fun String.getFile(mockIndex: Int = 0): File {
    val mockName = if (mockIndex > 0) "_${mockIndex}" else ""
    return File("in", "$this$mockName.txt")
}
