package de.dominik48n.generalnetwork.master

import org.apache.log4j.Logger

class MasterServer {

    val logger = Logger.getLogger("Master-Server")

    fun start() {
        this.logger.info("Starting Master Server...")
    }

    fun stop() {
        this.logger.info("Stopping Master Server...")
    }
}
