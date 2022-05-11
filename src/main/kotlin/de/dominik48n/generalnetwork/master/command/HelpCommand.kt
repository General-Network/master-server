package de.dominik48n.generalnetwork.master.command

import de.dominik48n.generalnetwork.master.MasterServer

class HelpCommand : Command {

    override fun execute(args: Array<String>) {
        MasterServer.INSTANCE.logger.info("All Commands:")

        for (name in MasterServer.INSTANCE.commandManager.commands.keys) {
            MasterServer.INSTANCE.logger.info(" > $name")
        }
    }
}
