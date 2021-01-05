package io.glossnyx.vibes.util

import io.glossnyx.vibes.network.packet.Packet
import io.glossnyx.vibes.network.packet.send
import net.minecraft.server.world.ServerWorld
import net.minecraft.world.World

fun World.sendAll(packet: Packet) = (this as? ServerWorld)?.players?.forEach { send(it, packet) }
