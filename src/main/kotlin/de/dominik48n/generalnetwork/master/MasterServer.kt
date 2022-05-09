package de.dominik48n.generalnetwork.master

import com.google.gson.GsonBuilder
import de.dominik48n.generalnetwork.master.network.DefaultNetworkProvider
import de.dominik48n.generalnetwork.master.network.handler.NetworkHandler
import de.dominik48n.generalnetwork.master.network.packet.handler.PacketDecoder
import de.dominik48n.generalnetwork.master.network.packet.handler.PacketEncoder
import org.apache.log4j.Logger

class MasterServer {

    companion object {
        lateinit var INSTANCE: MasterServer
    }

    val logger = Logger.getLogger("Master-Server")
    val gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

    lateinit var defaultNetworkProvider: DefaultNetworkProvider

    fun start() {
        this.logger.info("Starting Master Server...")

        INSTANCE = this

        this.prepareNettyServer()
    }

    fun stop() {
        this.logger.info("Stopping Master Server...")

        this.defaultNetworkProvider.nettyServer.stopServer()
    }

    private fun prepareNettyServer() {
        this.defaultNetworkProvider = DefaultNetworkProvider()
        this.defaultNetworkProvider.start()

        this.defaultNetworkProvider.nettyServer.startServer(9967) {
            it.pipeline()
                .addLast(PacketEncoder(this.defaultNetworkProvider.packetRegistry))
                .addLast(PacketDecoder(this.defaultNetworkProvider.packetRegistry))
                .addLast(NetworkHandler())
        }
    }
}
