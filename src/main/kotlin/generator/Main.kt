package generator

import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) = Main()
    .subcommands(Add(), ListC(), Scan(), Remove())
    .main(args)

