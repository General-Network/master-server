package de.dominik48n.generalnetwork.master.network.packet.`in`

import de.dominik48n.generalnetwork.master.MasterServer
import de.dominik48n.generalnetwork.master.config.Document
import de.dominik48n.generalnetwork.master.network.packet.Packet
import io.netty.channel.Channel
import java.util.*

class PacketInUpdateServer : Packet {

    private lateinit var players: Set<UUID>
    private lateinit var name: String

    override fun read(document: Document) {
        this.players = document.getStringValue("players").split(";").map { UUID.fromString(it) }.toSet()
        this.name = document.getStringValue("name")
    }

    override fun handle(channel: Channel) {
        (MasterServer.INSTANCE.servers[this.name] ?: return).players = this.players
    }
}
