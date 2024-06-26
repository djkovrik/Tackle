package com.sedsoftware.tackle

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.sedsoftware.tackle.domain.TacklePlatformTools
import com.sedsoftware.tackle.domain.model.AppClientData

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

        override fun getClientData(): AppClientData {
            val scheme = context.getString(R.string.app_scheme)
            val host = context.getString(R.string.app_host)
            return AppClientData(
                name = context.getString(R.string.app_name),
                uri = "$scheme://$host",
                scopes = context.getString(R.string.app_scopes),
                website = context.getString(R.string.app_website),
            )
        }
    }
