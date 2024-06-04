package com.sedsoftware.tackle.compose.core

import kotlin.math.PI

internal object Math {
    fun toRadians(deg: Double): Double = deg / DEG * PI
    fun toDegrees(rad: Double): Double = rad * DEG / PI

    private const val DEG = 180.0

}
