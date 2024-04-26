package com.sedsoftware.tackle.compose.core

import kotlin.math.PI

internal object Math {
    private const val MULT = 180.0

    fun toRadians(deg: Double): Double = deg / MULT * PI

    fun toDegrees(rad: Double): Double = rad * MULT / PI
}
