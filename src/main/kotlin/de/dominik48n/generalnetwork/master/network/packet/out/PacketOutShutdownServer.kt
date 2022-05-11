package de.dominik48n.generalnetwork.master.network.packet.out

import de.dominik48n.generalnetwork.master.config.Document
import de.dominik48n.generalnetwork.master.network.packet.Packet

class PacketOutShutdownServer : Packet {

    override fun write(): Document {
        return Document()
    }

}
