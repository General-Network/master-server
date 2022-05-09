package de.dominik48n.generalnetwork.master.network.packet

class PacketRegistry {

    private val incomingPackets: HashMap<Int, Class<out Packet>> = HashMap()
    private val outgoingPackets: HashMap<Int, Class<out Packet>> = HashMap()

    fun getIncomingPacketById(packetId: Int): Packet? {
        return this.incomingPackets[packetId]?.newInstance()
    }

    fun getIdByOutgoingPacket(packet: Packet): Int {
        var finished = -1

        for (entry in outgoingPackets) {
            if (entry.value == packet::class.java) finished = entry.key
        }

        return finished
    }

    fun addIncomingPacket(packetId: Int, packetClass: Class<out Packet>) {
        this.incomingPackets[packetId] = packetClass
    }

    fun addOutgoingPacket(packetId: Int, packetClass: Class<out Packet>) {
        this.outgoingPackets[packetId] = packetClass
    }
}
