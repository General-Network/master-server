package de.dominik48n.generalnetwork.master

import com.google.gson.GsonBuilder
import de.dominik48n.generalnetwork.master.config.Document
import de.dominik48n.generalnetwork.master.network.DefaultNetworkProvider
import de.dominik48n.generalnetwork.master.network.handler.NetworkHandler
import de.dominik48n.generalnetwork.master.network.packet.handler.PacketDecoder
import de.dominik48n.generalnetwork.master.network.packet.handler.PacketEncoder
import org.apache.log4j.Logger
import java.io.File

class MasterServer {

    companion object {
        lateinit var INSTANCE: MasterServer
    }

    val logger = Logger.getLogger("Master-Server")
    val gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

    lateinit var defaultNetworkProvider: DefaultNetworkProvider
    lateinit var masterConfig: Document

    fun start() {
        this.logger.info("Starting Master Server...")

        INSTANCE = this

        this.prepareConfig()
        this.prepareNettyServer()
    }

    fun stop() {
        this.logger.info("Stopping Master Server...")

        this.defaultNetworkProvider.nettyServer.stopServer()
    }

    private fun prepareConfig() {
        val configFile = File("master", "config.json")

        this.masterConfig = Document.read(configFile) ?: Document.create(configFile)
    }

    private fun prepareNettyServer() {
        this.defaultNetworkProvider = DefaultNetworkProvider()
        this.defaultNetworkProvider.start()

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
