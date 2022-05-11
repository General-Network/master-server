package de.dominik48n.generalnetwork.master.server

import de.dominik48n.generalnetwork.master.network.packet.Packet
import io.netty.channel.Channel

class Server(val name: String, private val channel: Channel, private val serverType: ServerType) {

    fun sendPacket(packet: Class<out Packet>) {
        this.channel.writeAndFlush(packet)
    }

}
