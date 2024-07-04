package com.sedsoftware.tackle

import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.intercept.bitmapMemoryCacheConfig
import com.seiko.imageloader.intercept.imageMemoryCacheConfig
import com.seiko.imageloader.intercept.painterMemoryCacheConfig
import okio.Path.Companion.toOkioPath
import java.io.File

fun generateImageLoader(): ImageLoader {
    return ImageLoader {
        components {
            setupDefaultComponents()
        }
        interceptor {
            bitmapMemoryCacheConfig {
                maxSize(size = 32 * 1024 * 1024)
            }
            imageMemoryCacheConfig {
                maxSize(size = 50)
            }
            painterMemoryCacheConfig {
                maxSize(size = 50)
            }
            diskCacheConfig {
                directory(getCacheDir().toOkioPath().resolve(child = "image_cache"))
                maxSizeBytes(size = 512L * 1024 * 1024)
            }
        }
    }
}

enum class OperatingSystem {
    Windows, Linux, MacOS, Unknown
}

private val currentOperatingSystem: OperatingSystem
    get() {
        val sys = System.getProperty("os.name").lowercase()
        return if (sys.contains("win")) {
            OperatingSystem.Windows
        } else if (sys.contains("nix") || sys.contains("nux") ||
            sys.contains("aix")
        ) {
            OperatingSystem.Linux
        } else if (sys.contains("mac")) {
            OperatingSystem.MacOS
        } else {
            OperatingSystem.Unknown
        }
    }

private fun getCacheDir() = when (currentOperatingSystem) {
    OperatingSystem.Windows -> File(System.getenv("AppData"), "$ApplicationName/cache")
    OperatingSystem.Linux -> File(System.getProperty("user.home"), ".cache/$ApplicationName")
    OperatingSystem.MacOS -> File(System.getProperty("user.home"), "Library/Caches/$ApplicationName")
    else -> throw IllegalStateException("Unsupported operating system")
}

private const val ApplicationName = "Tackle"
