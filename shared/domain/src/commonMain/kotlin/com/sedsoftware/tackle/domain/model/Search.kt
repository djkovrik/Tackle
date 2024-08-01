package com.sedsoftware.tackle.domain.model

data class Search(
    val accounts: List<Account>,
    val statuses: List<Status>,
    val hashtags: List<HashTag>,
)
