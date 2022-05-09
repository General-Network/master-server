package de.dominik48n.generalnetwork.master.network.packet.handler

import de.dominik48n.generalnetwork.master.config.Document
import de.dominik48n.generalnetwork.master.network.packet.PacketRegistry
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufInputStream
import io.netty.buffer.EmptyByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

class PacketDecoder(private val packetRegistry: PacketRegistry) : ByteToMessageDecoder() {

    override fun decode(channelHandlerContext: ChannelHandlerContext, byteBuf: ByteBuf, output: MutableList<Any>) {
        if (byteBuf is EmptyByteBuf) return

        val packetId = byteBuf.readInt()
        val packet = this.packetRegistry.getIncomingPacketById(packetId)
            ?: throw NullPointerException("Packet with ID($packetId) is null!")

        packet.read(Document.read(ByteBufInputStream(byteBuf).readUTF()))
        output.add(packet)
    }
}
