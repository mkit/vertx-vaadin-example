package com.vaadin.starter

import com.github.mcollovati.vertx.vaadin.VaadinVerticle
import io.vertx.core.Vertx
import net.corda.core.cordapp.CordappConfigException
import net.corda.core.node.AppServiceHub
import net.corda.core.node.services.CordaService
import net.corda.core.serialization.SingletonSerializeAsToken
import net.corda.core.utilities.seconds
import org.slf4j.LoggerFactory
import java.net.ServerSocket
import java.util.*
import kotlin.concurrent.schedule


@CordaService
class CordiumService(val serviceHub: AppServiceHub) : SingletonSerializeAsToken() {
    companion object {
        private const val NODE_EXPLORER_PORT_KEY = "cordiumPort"
        private val logger = LoggerFactory.getLogger(this::class.java)
        lateinit var instance: CordiumService
    }

    init {
        val config = serviceHub.getAppContext().config
        val port = try {
            config.get(NODE_EXPLORER_PORT_KEY) as Int
        } catch (e: CordappConfigException) {
            getFreePort()
        }
        Timer().schedule(1.seconds.toMillis()) {
            startServer(port)
        }
        instance = this
    }

    private fun startServer(port: Int) {
        val vertx = Vertx.vertx()
        vertx.deployVerticle(VaadinVerticle());
    }
}

fun getFreePort(): Int {
    return ServerSocket(0).use {
        it.localPort
    }
}
