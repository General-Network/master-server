package de.dominik48n.generalnetwork.master.network.packet.`in`

import de.dominik48n.generalnetwork.master.MasterServer
import de.dominik48n.generalnetwork.master.config.Document
import de.dominik48n.generalnetwork.master.network.packet.Packet
import de.dominik48n.generalnetwork.master.server.Server
import de.dominik48n.generalnetwork.master.server.ServerType
import io.netty.channel.Channel

class PacketInConnectServer : Packet {

    private lateinit var serverType: ServerType
    private lateinit var name: String

    override fun read(document: Document) {
        this.name = document.getStringValue("name")
        this.serverType = ServerType.valueOf(document.getStringValue("server-type"))
    }

    override fun handle(channel: Channel) {
        val server = Server(this.name, channel, this.serverType)

        MasterServer.INSTANCE.servers[this.name] = server
        MasterServer.INSTANCE.logger.info("Connected ${this.serverType.name} [${this.name}${channel.remoteAddress()}]")
    }

}
