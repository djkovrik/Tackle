package com.sedsoftware.tackle.main.gateway

import com.sedsoftware.tackle.domain.api.TacklePlatformTools
import com.sedsoftware.tackle.domain.model.AppLocale
import com.sedsoftware.tackle.status.StatusComponentGateways

internal class StatusComponentTools(
    private val platformTools: TacklePlatformTools,
) : StatusComponentGateways.Tools {

    override fun getCurrentLocale(): AppLocale = platformTools.getCurrentLocale()
    override fun openUrl(url: String) = platformTools.openUrl(url)
    override fun shareUrl(title: String, url: String) = platformTools.shareStatus(title, url)
}
