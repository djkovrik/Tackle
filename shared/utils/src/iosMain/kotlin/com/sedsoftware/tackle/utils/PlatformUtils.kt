package com.sedsoftware.tackle.utils

import platform.Foundation.NSUUID

actual fun generateUUID(): String = NSUUID().UUIDString()
