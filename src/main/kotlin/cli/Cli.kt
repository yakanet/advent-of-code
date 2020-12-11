package cli

import io.github.bonigarcia.wdm.WebDriverManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.io.Closeable
import java.io.File
import java.time.Year
import java.util.Scanner
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun main() {
    val sc = Scanner(System.`in`)
    var workspace = Workspace(Year.now())
    while (true) {
        println("Selected year: ${workspace.year}")
        println("1. Change year (${workspace.year})")
        println("2. Create new year workspace for ${workspace.year}")
        println("3. Update inputs from AdventOfCode website for ${workspace.year}")
        println("q. Exit")

        when (sc.next()) {
            "1" -> sc.getYear()?.let { workspace = Workspace(it) }
            "2" -> workspace.createWorkspace(EmptyGetter())
            "3" -> workspace.updateWorkspace(BrowserGetter(getConnectedBrowser(workspace.year)))
            "q" -> System.exit(0)
        }
        println()
    }
}


class Workspace(val year: Year) {
    private val resourceDir = File("in/$year")
    private val sourceDir = File("src/main/kotlin/advent$year")

    private val actions = mutableListOf<() -> Unit>()

    init {
        createDirectory(resourceDir)
        createDirectory(sourceDir)
    }

    private fun createDirectory(file: File) {
        if (!file.exists()) {
            println(">> Create directory $file")
            actions += { file.mkdirs() }
        }
    }

    fun addSource(name: String, override: Boolean = false, content: () -> String) {
        addFile(File(sourceDir, name), override, content)
    }

    fun addResource(name: String, override: Boolean = false, content: () -> String) {
        addFile(File(resourceDir, name), override, content)
    }

    private fun addFile(file: File, override: Boolean, content: () -> String) {
        if (!file.exists()) {
            println(">> Create file $file")
            actions += { file.createNewFile() }
        }
        if (file.exists() && override) {
            println(">> Update file $file")
            actions += { file.writeText(content()) }
        }
    }

    fun execute() {
        actions.forEach { it() }
        actions.clear()
    }
}

fun Workspace.createWorkspace(inputSource: InputGetter) {
    inputSource.use { inputGetter ->
        (1..25).forEach { day ->
            addResource("${day.normalize()}.txt", override = true) {
                inputGetter.get(year, day)
            }
            addSource("Advent${day.normalize()}.kt") {
                KOTLIN_TEMPLATE(year, day)
            }
        }
        execute()
    }
}

fun Workspace.updateWorkspace(inputSource: InputGetter) {
    inputSource.use { inputGetter ->
        (1..25).forEach { day ->
            addResource("${day.normalize()}.txt", override = true) {
                inputGetter.get(year, day)
            }
        }
        execute()
    }
}

val KOTLIN_TEMPLATE = { year: Year, day: Int ->
    """@file:Exercise($year, $day)
package advent$year

import common.*

// Link for the exercise: https://adventofcode.com/${year}/day/${day}
fun main() {
    val input = "$year/${day.normalize()}".getText()
}
"""
}

private fun Int.normalize() = toString().padStart(2, '0')

fun getConnectedBrowser(year: Year): WebDriver {
    WebDriverManager.chromedriver().setup()
    val driver = ChromeDriver(ChromeOptions().apply {
        addArguments("user-data-dir=advent-of-code")
    })
    driver.get("https://adventofcode.com/$year/auth/login")
    runBlocking {
        driver.waitUntilUrlEquals("https://adventofcode.com/$year")
    }
    return driver
}

suspend fun WebDriver.waitUntilUrlEquals(targetUrl: String) = suspendCoroutine<String> {
    GlobalScope.launch {
        do {
            delay(1000)
        } while (currentUrl!=targetUrl)
        it.resume(currentUrl)
    }
}

interface InputGetter : Closeable {
    fun get(year: Year, day: Int): String
}

class EmptyGetter : InputGetter {
    override fun get(year: Year, day: Int) = ""
    override fun close() {
    }
}

class BrowserGetter(private val browser: WebDriver) : InputGetter {
    override fun get(year: Year, day: Int): String {
        browser.get("https://adventofcode.com/$year/day/$day/input")
        return Jsoup.parse(browser.pageSource).text()
    }

    override fun close() {
        browser.close()
    }
}

fun Scanner.getYear(): Year? {
    print("Enter the Advent of code year : ")
    val year = Year.of(nextInt())
    return if (year.isAfter(Year.of(2014))) year else {
        System.err.println("Invalid year (must be after 2015)")
        System.err.flush()
        null
    }
}
