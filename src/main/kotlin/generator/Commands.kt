package generator

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import java.io.File
import java.nio.file.Files


class Main : CliktCommand() {
    override fun aliases() = mapOf(
        "a" to listOf("add"),
        "l" to listOf("list"),
        "s" to listOf("scan"),
        "r" to listOf("remove")
    )

    override fun run() = Unit
}

class Add : CliktCommand(help = "Adds entry to watched") {
    val name by option("-n", "--name", help = "Name of the show")
    val url by option("-u", "--url", help = "Url of show to be added").required()
    override fun run() {
        Repository.addShow(url, name)
        println("Now watching following:")
        renderList()
    }
}

class ListC : CliktCommand("List added shows", name = "list") {
    override fun run() = renderList()
}

class Scan : CliktCommand("Rescan for new entries") {
    val file by option("--file", "-f", "--output", "-o", help = "File where output RSS to. If '-' is defined will be printed to stdout intead of writing to file.").required()
    val prettyPrint by option("--pretty-print", "-p", help = "Pretty print code").flag("--np", "--no-pretty-print")
    override fun run() {
        Scanner.scan()
        val message = Generator.generateRSS(prettyPrint)
        if (file == "-") println(message)
        else Files.write(File(file).toPath(), message.lines())
    }
}

class Remove : CliktCommand("Remove subscription") {
    val name by option("-n", "--name", help = "Name of the show")
    val url by option("-u", "--url", help = "Url of show to be removed").required()
    override fun run() {
        Repository.removeShow(url, name)
        println("Now watching following:")
        renderList()
    }
}

private fun renderList() {
    val listShows = Repository.listShows()
    val asciiTable = AsciiTable()
    asciiTable.addColumn("name")
    asciiTable.addColumn("url")
    for ((url, name) in listShows) {
        asciiTable.addRow(name ?: "", url)
    }
    asciiTable.render()
}
