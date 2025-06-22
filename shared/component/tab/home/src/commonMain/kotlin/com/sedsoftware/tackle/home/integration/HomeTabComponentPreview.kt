package com.sedsoftware.tackle.home.integration

import com.sedsoftware.tackle.domain.model.Status
import com.sedsoftware.tackle.home.HomeTabComponent
import com.sedsoftware.tackle.statuslist.StatusListComponent
import com.sedsoftware.tackle.statuslist.integration.StatusListComponentPreview

class HomeTabComponentPreview(statuses: List<Status>, ) : HomeTabComponent {

    override val homeTimeline: StatusListComponent =
        StatusListComponentPreview(statuses)

    override fun onNewPostClick() = Unit
    override fun onScheduledPostsClick() = Unit
    override fun showCreatedStatus(status: Status) = Unit
}
