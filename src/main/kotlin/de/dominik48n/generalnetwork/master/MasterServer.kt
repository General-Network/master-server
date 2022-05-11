package de.dominik48n.generalnetwork.master

import com.google.gson.GsonBuilder
import de.dominik48n.generalnetwork.master.command.CommandManager
import de.dominik48n.generalnetwork.master.config.Document
import de.dominik48n.generalnetwork.master.network.DefaultNetworkProvider
import de.dominik48n.generalnetwork.master.network.handler.NetworkHandler
import de.dominik48n.generalnetwork.master.network.packet.`in`.PacketInConnectServer
import de.dominik48n.generalnetwork.master.network.packet.`in`.PacketInGetPlayers
import de.dominik48n.generalnetwork.master.network.packet.`in`.PacketInUpdateServer
import de.dominik48n.generalnetwork.master.network.packet.handler.PacketDecoder
import de.dominik48n.generalnetwork.master.network.packet.handler.PacketEncoder
import de.dominik48n.generalnetwork.master.network.packet.out.PacketOutShutdownServer
import de.dominik48n.generalnetwork.master.server.Server
import org.apache.log4j.Logger
import java.io.File
import kotlin.system.exitProcess

class MasterServer {

    companion object {
        lateinit var INSTANCE: MasterServer
    }

    val commandManager = CommandManager()
    val servers = HashMap<String, Server>()
    val logger = Logger.getLogger("Master-Server")
    val gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

    lateinit var defaultNetworkProvider: DefaultNetworkProvider
    lateinit var masterConfig: Document

    fun start() {
        this.logger.info("Starting Master Server...")

        INSTANCE = this

        this.commandManager.start()

        this.prepareConfig()
        this.prepareNettyServer()
    }

    fun stop() {
        this.logger.info("Stopping Master Server...")

        this.defaultNetworkProvider.nettyServer.stopServer()

        exitProcess(0)
    }

    private fun prepareConfig() {
        val configFile = File("master", "config.json")

        this.masterConfig = Document.read(configFile) ?: Document.create(configFile)
    }

    private fun prepareNettyServer() {
        this.defaultNetworkProvider = DefaultNetworkProvider()
        this.defaultNetworkProvider.start()

        this.defaultNetworkProvider.packetRegistry.addIncomingPacket(1, PacketInConnectServer::class.java)
        this.defaultNetworkProvider.packetRegistry.addIncomingPacket(2, PacketInUpdateServer::class.java)
        this.defaultNetworkProvider.packetRegistry.addIncomingPacket(3, PacketInGetPlayers::class.java)

        this.defaultNetworkProvider.packetRegistry.addOutgoingPacket(4, PacketOutShutdownServer::class.java)

        this.defaultNetworkProvider.nettyServer.startServer(
            this.masterConfig.getDocument("network").getIntValue("master-port")
        ) {
            it.pipeline()
                .addLast(PacketEncoder(this.defaultNetworkProvider.packetRegistry))
                .addLast(PacketDecoder(this.defaultNetworkProvider.packetRegistry))
                .addLast(NetworkHandler())
        }
    }
}
