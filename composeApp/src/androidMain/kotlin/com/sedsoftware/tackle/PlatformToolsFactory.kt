package com.sedsoftware.tackle

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.sedsoftware.tackle.utils.OAuthFlowException
import com.sedsoftware.tackle.utils.TacklePlatformTools
import com.sedsoftware.tackle.utils.model.AppClientData
import com.sedsoftware.tackle.utils.trimUrl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Suppress("FunctionName")
fun PlatformToolsFactory(context: Context): TacklePlatformTools =
    object : TacklePlatformTools {
        private val _oauthResponseFlow: MutableStateFlow<Result<String>> = MutableStateFlow(Result.success(""))

        override fun openUrl(url: String?) {
            val uri = url?.let { Uri.parse(it) } ?: return
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = uri
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }

        override fun openAuthWebsite(domain: String, clientId: String) {
            val clientData: AppClientData = getClientData()
            val authority = domain.trimUrl()

            val uri: Uri = Uri.Builder()
                .scheme("https")
                .authority(authority)
                .appendPath("oauth")
                .appendPath("authorize")
                .appendQueryParameter("response_type", "code")
                .appendQueryParameter("client_id", clientId)
                .appendQueryParameter("redirect_uri", clientData.uri)
                .appendQueryParameter("scope", clientData.scopes)
                .build()

            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = uri
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }

        override fun handleOAuthResponse(code: String?, error: String?, description: String?) {
            if (code.isNullOrEmpty() || !error.isNullOrEmpty()) {
                _oauthResponseFlow.value = Result.failure(OAuthFlowException(error, description))
                return
            }

            _oauthResponseFlow.value = Result.success(code)
        }

        override fun getOAuthResponseFlow(): StateFlow<Result<String>> = _oauthResponseFlow

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
