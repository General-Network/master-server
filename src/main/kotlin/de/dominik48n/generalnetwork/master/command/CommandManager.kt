package de.dominik48n.generalnetwork.master.command

import de.dominik48n.generalnetwork.master.MasterServer
import java.io.BufferedReader
import java.io.InputStreamReader

class CommandManager {

    val commands = HashMap<String, Command>()

    init {
        this.commands["help"] = HelpCommand()
    }

    fun start() {
        Thread({
            val bufferedReader = BufferedReader(InputStreamReader(System.`in`))

            while (true) {
                val fullString = bufferedReader.readLine().split(" ").toTypedArray()

                if (fullString[0] == "") continue
                val command = this.commands[fullString[0]]

                if (command == null) {
                    MasterServer.INSTANCE.logger.info("Command not found. Please try \"help\"")
                    continue
                }

                command.execute(fullString.copyOfRange(1, fullString.size))
            }
        }, "master-command").start()
    }
}
