package de.dominik48n.generalnetwork.master.server

import de.dominik48n.generalnetwork.master.network.packet.Packet
import io.netty.channel.Channel
import java.util.*
import kotlin.collections.HashSet

class Server(val name: String, private val channel: Channel, val serverType: ServerType, var players: Set<UUID>) {

    constructor(name: String, channel: Channel, serverType: ServerType) : this(name, channel, serverType, HashSet())

    fun sendPacket(packet: Packet) {
        this.channel.writeAndFlush(packet)
    }

}
