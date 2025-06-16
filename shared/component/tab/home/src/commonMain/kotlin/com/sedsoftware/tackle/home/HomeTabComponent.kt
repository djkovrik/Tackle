package com.sedsoftware.tackle.home

import com.sedsoftware.tackle.statuslist.StatusListComponent

interface HomeTabComponent {
    val homeTimeline: StatusListComponent

    fun onNewPostClick()
    fun onScheduledPostsClick()
}
