package generator

import com.beust.jcommander.JCommander
import kotlinx.dnq.query.isEmpty
import java.io.File
import java.nio.file.Files
import kotlin.system.exitProcess

fun main(vararg args: String) {
    val add = CommandAdd()
    val remove = CommandRemove()
    val scan = CommandScan()
    val jcom = JCommander.newBuilder()
        .addCommand("add", add, "a")
        .addCommand("list", CommandList(), "l")
        .addCommand("help", CommandHelp(), "h", "usage")
        .addCommand("remove", remove, "r", "delete")
        .addCommand("scan", scan, "s")
        .build()
    if (args.isEmpty()) {
        jcom.usage()
        exitProcess(0)
    }
    store.transactional { if (Channel.all().isEmpty) init() }
    jcom.parse(*args)
    if (jcom.parsedCommand == "help") {
        jcom.usage()
        exitProcess(0)
    }
    if (jcom.parsedCommand == "add") {
        Repository.addShow(add.url, add.name)
        println("Now watching following:")
        renderList()
        exitProcess(0)
    }
    if (jcom.parsedCommand == "remove") {
        Repository.removeShow(remove.url, remove.name)
        println("Now watching following:")
        renderList()
        exitProcess(0)
    }
    if (jcom.parsedCommand == "list") {
        renderList()
        exitProcess(0)
    }
    if (jcom.parsedCommand == "scan") {
        Scanner.scan()
        val message = Generator.generateRSS(scan.prettyPrint)
        if (scan.file == "-") println(message)
        else Files.write(File(scan.file).toPath(), message.lines())
        exitProcess(0)
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

private fun init() {
    store.transactional {
        Channel.new {
            title = "Lutik"
            pubDate = pubDate()
            description = "Канал Яскъер"
            link = "https://lutik.tv"
        }
    }
}
