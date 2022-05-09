package de.dominik48n.generalnetwork.master.bootstrap

import de.dominik48n.generalnetwork.master.MasterServer

fun main() {
    val masterServer = MasterServer()

    Thread({
        masterServer.start()

        Runtime.getRuntime().addShutdownHook(Thread({
            masterServer.stop()
        }, "master-server-stop"))
    }, "master-server-main").start()
}
