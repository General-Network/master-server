package de.dominik48n.generalnetwork.master.command

import de.dominik48n.generalnetwork.master.MasterServer
import de.dominik48n.generalnetwork.master.network.packet.out.PacketOutShutdownServer

class ShutdownCommand : Command {

    override fun execute(args: Array<String>) {

        if (args.isEmpty()) {
            MasterServer.INSTANCE.logger.info("Use: shutdown master/server (Server)")
            return
        }

        when (args[0].lowercase()) {

            "master" -> {
                MasterServer.INSTANCE.logger.info("Stopping master..")
                MasterServer.INSTANCE.stop()
            }

            "server" -> {
                if (args.size < 2) {
                    MasterServer.INSTANCE.logger.info("Use: shutdown server <Name>")
                    return
                }
                val server = MasterServer.INSTANCE.servers[args[1]]

                if (server == null) {
                    MasterServer.INSTANCE.logger.info("Server not found!")
                    return
                }

                MasterServer.INSTANCE.logger.info("Stopping server..")
                server.sendPacket(PacketOutShutdownServer())
            }
        }
    }
}
