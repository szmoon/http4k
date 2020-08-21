package org.http4k.h4k.example.test.functional

import org.http4k.cloudnative.env.Environment
import org.http4k.cloudnative.env.Environment.Companion.EMPTY
import org.http4k.h4k.example.lib.Discovery
import org.http4k.h4k.example.lib.H4KCluster
import org.http4k.h4k.example.main.ExternalServiceId
import org.http4k.h4k.example.main.InternalServiceId
import org.http4k.h4k.example.main.external.Doubler
import org.http4k.h4k.example.main.external.Reverser
import org.http4k.h4k.example.main.internal.Main
import org.http4k.h4k.example.main.internal.Proxy
import org.http4k.h4k.example.test.external.FakeDoubler
import org.http4k.h4k.example.test.external.FakeReverser
import org.http4k.h4k.example.test.main.TestEvents

class TestEnvironment(private val env: Environment = EMPTY) : Discovery<InternalServiceId>, Environment by env {
    val events = TestEvents()

    val fakeReverser = FakeReverser()
    val fakeDoubler = FakeDoubler()

    private val externals = H4KCluster<ExternalServiceId>()
        .install(Reverser.ID) { fakeReverser }
        .install(Doubler.ID) { fakeDoubler }

    private val cluster = H4KCluster<InternalServiceId>()
        .install(Main.ID) { Main(env, events, externals) }
        .install(Proxy.ID) { Proxy(env, events, it) }

    fun start() = apply {
        externals.start()
        cluster.start()
    }

    fun stop() = apply {
        cluster.stop()
        externals.stop()
    }

    override fun lookup(id: InternalServiceId) = cluster.lookup(id)
}
