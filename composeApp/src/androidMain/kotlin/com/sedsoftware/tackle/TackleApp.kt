package com.sedsoftware.tackle

import android.app.Application
import com.github.panpf.sketch.PlatformContext
import com.github.panpf.sketch.SingletonSketch
import com.github.panpf.sketch.Sketch
import com.github.panpf.sketch.cache.MemoryCache
import com.github.panpf.sketch.fetch.KtorHttpUriFetcher
import com.github.panpf.sketch.fetch.internal.KtorHttpUriFetcherProvider
import com.github.panpf.sketch.http.KtorStack
import io.ktor.client.HttpClient

class TackleApp : Application(), SingletonSketch.Factory {

    override fun createSketch(context: PlatformContext): Sketch =
        Sketch.Builder(this).apply {
            memoryCache(
                MemoryCache.Builder(context)
                    .maxSizePercent(0.5)
                    .build()
            )

            addIgnoreFetcherProvider(KtorHttpUriFetcherProvider::class)
            addComponents {
                val httpClient = HttpClient {}
                val httpStack = KtorStack(httpClient)
                addFetcher(KtorHttpUriFetcher.Factory(httpStack))
            }
        }.build()
}
