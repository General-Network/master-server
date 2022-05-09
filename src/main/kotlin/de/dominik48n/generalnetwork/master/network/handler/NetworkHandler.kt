package de.dominik48n.generalnetwork.master.network.handler

import de.dominik48n.generalnetwork.master.MasterServer
import de.dominik48n.generalnetwork.master.network.packet.Packet
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

class NetworkHandler : SimpleChannelInboundHandler<Packet>() {

    override fun channelRead0(channelHandlerContext: ChannelHandlerContext, packet: Packet) {
        packet.handle(channelHandlerContext.channel())
    }

    override fun channelInactive(channelHandlerContext: ChannelHandlerContext) {
        MasterServer.INSTANCE.logger.info("A channel has disconnected!")
    }
}
