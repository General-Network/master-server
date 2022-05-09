package de.dominik48n.generalnetwork.master.network.packet

import de.dominik48n.generalnetwork.master.config.Document
import io.netty.channel.Channel

interface Packet {

    fun read(document: Document) {}

    fun write(): Document {
        return Document().appendString("message", "no_value")
    }

    fun handle(channel: Channel) {}
}
