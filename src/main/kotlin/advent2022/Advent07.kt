@file:Puzzle(2022, 7)

package advent2022

import common.Puzzle
import common.getLines
import java.util.function.Predicate

const val DISK_SIZE = 70_000_000
const val UPDATE_SIZE = 30_000_000

// Link for the exercise: https://adventofcode.com/2022/day/7
fun main() {
    val lines = "2022/07".getLines()
    val root = Folder("/").parse(lines)

    // Part 1
    println(root.filterBy { it.getSize() <= 100_000 }.sumOf { it.getSize() })

    // Part 2
    println(root.getSize().let { rootSize ->
        root.flatten().sortedBy { it.getSize() }
            .first { DISK_SIZE - rootSize + it.getSize() > UPDATE_SIZE }.getSize()
    })
}

private fun Folder.filterBy(predicate: Predicate<Folder>): Sequence<Folder> = sequence {
    val stack = ArrayDeque<Folder>()
    stack.add(this@filterBy)
    while (stack.isNotEmpty()) {
        val folder = stack.removeFirst()
        if (predicate.test(folder)) {
            this.yield(folder)
        }
        stack.addAll(folder.files.filterIsInstance(Folder::class.java))
    }
}

private fun Folder.parse(lines: List<String>): Folder {
    var currentDirectory: Folder = this
    for (line in lines) {
        when {
            line.startsWith("$ ") -> {
                val command = line.split(" ").drop(1).toCommand()
                if (command is ChangeDirectory) {
                    currentDirectory = if (command.path == this.name) {
                        this
                    } else if (command.path == "..") {
                        currentDirectory.parent!!
                    } else if (Folder(command.path) in currentDirectory) {
                        currentDirectory[command.path]!!
                    } else {
                        Folder(command.path, currentDirectory)
                    }
                }
            }

            else -> {
                val file = line.toFile(currentDirectory)
                currentDirectory + file
            }
        }
    }
    return this
}

private fun Folder.flatten(): List<Folder> {
    return filterBy { true }.toList()
}

private fun String.toFile(parent: Folder?): File {
    val (param, name) = split(" ".toRegex(), 2)
    return if (param == "dir") {
        Folder(name, parent)
    } else {
        File(name, param.toInt())
    }
}

private open class File(val name: String, private val size: Int) {
    open fun getSize() = size
}

private class Folder(name: String, val parent: Folder? = null) : File(name, 0) {

    val files = mutableListOf<File>()

    override fun getSize(): Int {
        return files.sumOf { it.getSize() }
    }

    operator fun contains(folder: Folder) = files.filterIsInstance(Folder::class.java)
        .any { file -> file.name == folder.name }

    operator fun get(path: String) = files.filterIsInstance(Folder::class.java)
        .find { file -> file.name == path }

    operator fun plus(file: File) {
        files.add(file)
    }
}

private fun printTree(file: File, level: Int = 0) {
    val type = if (file is Folder) "dir" else "file"
    println("  ".repeat(level) + " - ${file.name} ($type, ${file.getSize()})")
    if (file is Folder) {
        for (f in file.files) {
            printTree(f, level + 1)
        }
    }
}

private fun List<String>.toCommand() = when (get(0)) {
    "cd" -> ChangeDirectory(get(1))
    "ls" -> ListDirectory()
    else -> TODO("No command match for for $this")
}

private sealed interface Command
private data class ChangeDirectory(val path: String) : Command
private class ListDirectory : Command {
    private val files = mutableListOf<File>()

    operator fun plus(file: File) {
        files.add(file)
    }
}
