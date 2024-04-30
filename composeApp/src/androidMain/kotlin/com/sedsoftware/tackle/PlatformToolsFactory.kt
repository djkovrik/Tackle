package com.sedsoftware.tackle

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.sedsoftware.tackle.utils.TacklePlatformTools

@Suppress("FunctionName")
fun PlatformToolsFactory(context: Context): TacklePlatformTools =
    object : TacklePlatformTools {
        override fun openUrl(url: String?) {
            val uri = url?.let { Uri.parse(it) } ?: return
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = uri
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }
