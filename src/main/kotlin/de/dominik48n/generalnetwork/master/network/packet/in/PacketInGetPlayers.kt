package de.dominik48n.generalnetwork.master.network.packet.`in`

import de.dominik48n.generalnetwork.master.MasterServer
import de.dominik48n.generalnetwork.master.config.Document
import de.dominik48n.generalnetwork.master.network.packet.Packet
import io.netty.channel.Channel
import java.util.*
import kotlin.collections.HashSet

class PacketInGetPlayers : Packet {

    private lateinit var serverNames: Array<String>

    private val players = HashSet<UUID>()

    override fun read(document: Document) {

        if (!document.contains("servers")) {
            this.serverNames = emptyArray()
            return
        }

        this.serverNames = document.getStringValue("servers").split(";").toTypedArray()
    }

    override fun write(): Document {
        return Document().appendString("players", this.players.joinToString(";"))
    }

    override fun handle(channel: Channel) {
        for (server in MasterServer.INSTANCE.servers.values) {
            if (!this.serverNames.contains(server.name)) continue

            this.players.addAll(server.players)
        }
    }

}
