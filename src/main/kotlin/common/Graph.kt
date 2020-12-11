package common

import java.awt.Desktop
import java.nio.file.Files
import java.util.ArrayDeque

// Graph
class Graph<T> {
    val adjacencyMap = HashMap<T, HashSet<T>>()
    fun addEdge(sourceVertex: T, destinationVertex: T, uniDirection: Boolean = false) {
        adjacencyMap.computeIfAbsent(sourceVertex) { HashSet() }.add(destinationVertex)
        if (!uniDirection) {
            adjacencyMap.computeIfAbsent(destinationVertex) { HashSet() }.add(sourceVertex)
        }
    }

    override fun toString(): String = adjacencyMap.keys.map { "$it -> ${adjacencyMap[it]}" }.joinToString("\n")
}

fun <T> Graph<T>.depthFirstTraversal(startNode: T): List<T> {
    val visitedSet = mutableSetOf<T>()
    val traversalList = mutableListOf<T>()
    val stack = ArrayDeque<T>()

    // Initial step -> add the startNode to the stack.
    stack.add(startNode)

    // Traverse the graph.
    while (stack.isNotEmpty()) {
        // Pop the node off the top of the stack.
        val currentNode = stack.removeFirst()
        if (!visitedSet.contains(currentNode)) {
            // Store this for the result.
            traversalList.add(currentNode)
            // Mark the current node visited and add to the traversal list.
            visitedSet.add(currentNode)
            // Add nodes in the adjacency map.
            adjacencyMap[currentNode]?.forEach { node ->
                stack.add(node)
            }
        }
    }
    return traversalList.toList()
}

fun <T> Graph<T>.distance(startNode: T, maxDepth: Int = Int.MAX_VALUE): Map<T, Int> {
    val visitedSet = mutableSetOf<T>()
    val traversalList = mutableListOf<T>()
    val deck = ArrayDeque<T>()

    val depthMap = mutableMapOf<T, Int>()

    // Initial step -> add the startNode to the queue.
    deck.add(startNode)
    depthMap[startNode] = 0

    // Traverse the graph
    while (deck.isNotEmpty()) {
        val currentNode = deck.removeFirst()
        val currentDepth = depthMap[currentNode]!!
        if (currentDepth <= maxDepth) {
            if (!visitedSet.contains(currentNode)) {
                visitedSet.add(currentNode)
                traversalList.add(currentNode)
                adjacencyMap[currentNode]?.forEach { node ->
                    deck.add(node)
                    if (!depthMap.containsKey(node)) {
                        depthMap[node] = currentDepth + 1
                    }
                }
            }
        }
    }
    return depthMap.toMap()
}

fun <T> Graph<T>.toGraphviz(nodeName: (node: T) -> String) {
    val builder = StringBuilder()
    builder.append("digraph G {\n")
    adjacencyMap.forEach { node ->
        node.value.forEach {
            builder.append("\"${nodeName(node.key)}\" -> \"${nodeName(it)}\";\n")
        }
    }
    builder.append("\n}")
    val dotFile = Files.createTempFile("graphviz", ".dot").toFile().apply {
        writeText(builder.toString())
    }
    val outputFile = Files.createTempFile("graphviz", ".png").toFile()
    val process = ProcessBuilder("dot", "-Tpng", dotFile.absolutePath, "-o", outputFile.absolutePath).apply {
        redirectError()
    }.start()
    process.waitFor()
    process.errorStream.copyTo(System.err)
    Desktop.getDesktop().open(outputFile)
}
