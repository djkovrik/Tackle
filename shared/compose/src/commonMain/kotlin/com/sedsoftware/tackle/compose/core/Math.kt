package com.sedsoftware.tackle.compose.core

import kotlin.math.PI

internal object Math {
    fun toRadians(deg: Double): Double = deg / 180.0 * PI

    fun toDegrees(rad: Double): Double = rad * 180.0 / PI
}
