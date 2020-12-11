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
    val year = sc.getYear()
    val remoteInput = sc.getRemoteInput()

    val resourceDir = File("in/$year")
    val sourceDir = File("src/main/kotlin/advent$year")

    val inputGetter = if (remoteInput)
        BrowserGetter(getConnectedBrowser(year))
    else
        EmptyGetter()
    inputGetter.use {
        val actions = mutableListOf<Preparable>(
            CreateDirectory(resourceDir),
            CreateDirectory(sourceDir)
        )
        (1..25).forEach { day ->
            actions += WriteFile(File(resourceDir, "${day.normalize()}.txt"), override = true) {
                inputGetter.get(year, day)
            }
            actions += WriteFile(File(sourceDir, "Advent${day.normalize()}.kt")) {
                KOTLIN_TEMPLATE(year, day)
            }
        }
        println("\n\n The program will perform the following actions :")
        val executions = actions.map { it.prepare() }
        //if (sc.isAgree())
            executions.forEach { it() }
    }
}


interface Preparable {
    fun prepare(): () -> Unit
}

class CreateDirectory(private val file: File) : Preparable {
    override fun prepare(): () -> Unit {
        if (!file.exists()) {
            println(">> Create directory $file")
            return { file.mkdirs() }
        }
        return noop()
    }
}

class WriteFile(private val file: File, val override: Boolean = false, private val content: () -> String) : Preparable {
    override fun prepare(): () -> Unit {
        if (file.exists() && override) {
            println(">> Update file $file")
            return {
                file.writeText(content())
            }
        }
        if (!file.exists()) {
            println(">> Create file $file")
            return {
                file.createNewFile()
                file.writeText(content())
            }
        }
        return noop()
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
    WebDriverManager.chromedriver().setup();
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

fun Scanner.getYear(): Year {
    print("Enter the Advent of code year : ")
    return Year.of(nextInt())
}

private fun Scanner.getRemoteInput(): Boolean {
    print("Load inputs using browser (y/n) ?")
    return next()=="y"
}

private fun Scanner.isAgree(): Boolean {
    print("Do you agree (y/n) ?")
    return next()=="y"
}

private fun noop() = {}
