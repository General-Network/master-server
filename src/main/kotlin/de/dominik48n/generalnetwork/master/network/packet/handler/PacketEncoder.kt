package de.dominik48n.generalnetwork.master.network.packet.handler

import de.dominik48n.generalnetwork.master.network.packet.Packet
import de.dominik48n.generalnetwork.master.network.packet.PacketRegistry
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufOutputStream
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

class PacketEncoder(private val packetRegistry: PacketRegistry) : MessageToByteEncoder<Packet>() {

    override fun encode(channelHandlerContext: ChannelHandlerContext, packet: Packet, byteBuf: ByteBuf) {
        val packetId = this.packetRegistry.getIdByOutgoingPacket(packet)

        if (packetId == -1) {
            NullPointerException("ID of Packet(${packet::class.java.simpleName}) was not found!").printStackTrace()
            return
        }

        byteBuf.writeInt(packetId)
        ByteBufOutputStream(byteBuf).writeUTF(packet.write().getAsString())
    }
}
