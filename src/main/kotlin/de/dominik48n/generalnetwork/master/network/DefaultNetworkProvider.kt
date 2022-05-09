package de.dominik48n.generalnetwork.master.network

import de.dominik48n.generalnetwork.master.network.packet.PacketRegistry
import de.dominik48n.generalnetwork.master.network.server.NettyServer

class DefaultNetworkProvider : NetworkProvider {

    lateinit var packetRegistry: PacketRegistry
    lateinit var nettyServer: NettyServer

    override fun start() {
        this.packetRegistry = PacketRegistry()
        this.nettyServer = NettyServer()
    }
}
