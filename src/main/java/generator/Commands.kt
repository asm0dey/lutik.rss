package generator

import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters


@Parameters(commandDescription = "Record changes to the repository")
class CommandAdd {
    @Parameter(description = "Url of show to be added", required = true, names = ["--url", "-u"], arity = 1)
    lateinit var url: String
    @Parameter(description = "Name of the show", required = false, names = ["--name", "-n"], arity = 1)
    var name: String? = null

}

@Parameters(commandDescription = "Help")
class CommandHelp

@Parameters(commandDescription = "List added shows")
class CommandList

@Parameters(commandDescription = "Rescan for new entries")
class CommandScan {
    @Parameter(
        description = "File where output RSS to",
        required = true,
        names = ["--file", "-f", "--output", "-o"],
        arity = 1
    )
    lateinit var file: String
    @Parameter(description = "Pretty print code", names = ["--pretty-print", "-p"], arity = 1)
    var prettyPrint = false
}

@Parameters(commandDescription = "Remove subscription")
class CommandRemove {
    @Parameter(description = "Url of show to be removed", names = ["--url", "-u"], arity = 1)
    var url: String? = null
    @Parameter(description = "Name of the show", required = false, names = ["--name", "-n"], arity = 1)
    var name: String? = null
}

