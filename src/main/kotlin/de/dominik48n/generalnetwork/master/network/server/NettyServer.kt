package de.dominik48n.generalnetwork.master.network.server

import de.dominik48n.generalnetwork.master.MasterServer
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelInitializer
import io.netty.channel.EventLoopGroup
import io.netty.channel.epoll.Epoll
import io.netty.channel.epoll.EpollEventLoopGroup
import io.netty.channel.epoll.EpollServerSocketChannel
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.SelfSignedCertificate
import java.security.cert.CertificateException
import java.util.function.Consumer
import javax.net.ssl.SSLException

class NettyServer {

    lateinit var bossGroup: EventLoopGroup
    lateinit var workerGroup: EventLoopGroup

    fun startServer(port: Int, callback: Consumer<Channel>) {
        val epoll = Epoll.isAvailable()
        val sslContext = this.prepareCertificate()

        this.bossGroup = if (epoll) EpollEventLoopGroup() else NioEventLoopGroup()
        this.workerGroup = if (epoll) EpollEventLoopGroup() else NioEventLoopGroup()

        val channelFuture = ServerBootstrap()
            .group(this.bossGroup, this.workerGroup)
            .channel(if (epoll) EpollServerSocketChannel::class.java else NioServerSocketChannel::class.java)
            .childHandler(object : ChannelInitializer<Channel>() {
                override fun initChannel(channel: Channel) {
                    if (sslContext != null) channel.pipeline().addLast(sslContext.newHandler(channel.alloc()))
                    callback.accept(channel)
                }
            }).bind(port).syncUninterruptibly()

        MasterServer.INSTANCE.logger.info("Server successfully started on (127.0.0.1:$port)")
        channelFuture.channel().closeFuture().syncUninterruptibly()
    }

    fun stopServer() {
        this.workerGroup.shutdownGracefully()
        this.bossGroup.shutdownGracefully()
    }

    private fun prepareCertificate(): SslContext? {
        var sslContext: SslContext? = null

        try {
            val selfSignedCertificate = SelfSignedCertificate()

            sslContext = SslContextBuilder
                .forServer(selfSignedCertificate.certificate(), selfSignedCertificate.privateKey())
                .build()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: SSLException) {
            e.printStackTrace()
        }

        return sslContext
    }
}
