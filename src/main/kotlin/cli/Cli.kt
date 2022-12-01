package cli

import com.microsoft.playwright.BrowserContext
import com.microsoft.playwright.BrowserType.LaunchPersistentContextOptions
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import java.io.Closeable
import java.io.File
import java.nio.file.Path
import java.time.LocalDate
import java.time.Year
import java.util.*
import kotlin.system.exitProcess

fun main() {
    val sc = Scanner(System.`in`)
    var workspace = Workspace(Year.now())
    while (true) {
        println("Selected year: ${workspace.year}")
        println("1. Change year (${workspace.year})")
        println("2. Download every puzzle for year ${workspace.year}")
        println("3. Download today's puzzle")
        println("4. Download one day puzzle (${workspace.year})")
        println("q. Exit")

        when (sc.next()) {
            "1" -> sc.getYear()?.let { workspace = Workspace(it) }
            "2" -> workspace.createWorkspace(
                1..25,
                getConnectedBrowser(workspace.year)
            )
            "3" -> Workspace(Year.now()).createWorkspace(
                LocalDate.now().dayOfMonth,
                getConnectedBrowser(workspace.year)
            )
            "4" -> workspace.createWorkspace(
                sc.getDay(),
                getConnectedBrowser(workspace.year)
            )
            "q" -> exitProcess(0)
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
            actions += { file.writeText(content()) }
        } else if (override) {
            println(">> Update file $file")
            actions += { file.writeText(content()) }
        }
    }

    fun execute() {
        actions.forEach { it() }
        actions.clear()
    }
}

fun Workspace.createWorkspace(day: Int, inputSource: InputGetter) =
    createWorkspace(day..day, inputSource)

fun Workspace.createWorkspace(dayRange: IntRange, inputSource: InputGetter) {
    inputSource.use { inputGetter ->
        dayRange.forEach { day ->
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

val KOTLIN_TEMPLATE = { year: Year, day: Int ->
    """@file:Puzzle($year, $day)
package advent$year

import common.*

// Link for the exercise: https://adventofcode.com/${year}/day/${day}
fun main() {
    val input = "$year/${day.normalize()}".getText()
}
"""
}

private fun Int.normalize() = toString().padStart(2, '0')

object SimpleBrowser : Closeable {
    private val playwright = Playwright.create()!!
    private var context: BrowserContext? = null
    private var page: Page? = null
    private val path = Path.of(".")

    private fun getBrowser(): BrowserContext {
        if (context == null) {
            val option = LaunchPersistentContextOptions()
                .setHeadless(false)
            context = playwright.chromium().launchPersistentContext(path, option)
        }
        return context!!
    }

    fun getPage(): Page {
        page = getBrowser().newPage()
        return page!!
    }

    override fun close() {
        if (context != null) {
            context!!.close()
        }
        playwright.close()
    }

    fun store() {
        getBrowser().storageState()
    }
}

fun getConnectedBrowser(year: Year): InputGetter {
    val page = SimpleBrowser.getPage()
    page.navigate("https://adventofcode.com/$year/day/1/input")
    if (page.content().contains("Please log in")) {
        page.navigate("https://adventofcode.com/$year/auth/login")
        page.waitForURL { url -> url == "https://adventofcode.com/$year" || url == "https://adventofcode.com/$year/day/1/input" }
        SimpleBrowser.store()
    }
    return BrowserGetter(page)
}

interface InputGetter : Closeable {
    fun get(year: Year, day: Int): String
}

class BrowserGetter(private val page: Page) : InputGetter {
    override fun get(year: Year, day: Int): String {
        page.navigate("https://adventofcode.com/$year/day/$day/input")
        return page.content()
    }

    override fun close() {
        page.close()
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

private fun Scanner.getDay(): Int {
    print("Enter the day : ")
    val day = nextInt()
    return if (day in 1..25) {
        day
    } else {
        System.err.println("Invalid day should be between 1 and 25")
        System.err.flush()
        1
    }
}
