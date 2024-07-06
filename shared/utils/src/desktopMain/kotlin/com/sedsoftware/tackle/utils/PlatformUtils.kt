package com.sedsoftware.tackle.utils

import java.util.UUID

actual fun generateUUID(): String = UUID.randomUUID().toString()
