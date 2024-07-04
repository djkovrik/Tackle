package com.sedsoftware.tackle

import android.content.Context
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.intercept.bitmapMemoryCacheConfig
import com.seiko.imageloader.intercept.imageMemoryCacheConfig
import com.seiko.imageloader.intercept.painterMemoryCacheConfig
import com.seiko.imageloader.option.androidContext
import okio.Path.Companion.toOkioPath

fun generateImageLoader(context: Context): ImageLoader {
    return ImageLoader {
        options {
            androidContext(context.applicationContext)
        }
        components {
            setupDefaultComponents()
        }
        interceptor {
            bitmapMemoryCacheConfig {
                maxSizePercent(context = context, percent = 0.25)
            }
            imageMemoryCacheConfig {
                maxSize(size = 50)
            }
            painterMemoryCacheConfig {
                maxSize(size = 50)
            }
            diskCacheConfig {
                directory(context.cacheDir.resolve("image_cache").toOkioPath())
                maxSizeBytes(size = 512L * 1024 * 1024)
            }
        }
    }
}
