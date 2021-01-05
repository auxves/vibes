package io.glossnyx.vibes.sound

interface PositionProvider {
	fun getX(): Double
	fun getY(): Double
	fun getZ(): Double
}