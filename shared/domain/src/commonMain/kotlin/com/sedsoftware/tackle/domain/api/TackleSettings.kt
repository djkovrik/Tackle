package com.sedsoftware.tackle.domain.api

interface TackleSettings {
    var domainNormalized: String
    var domainShort: String
    var clientId: String
    var clientSecret: String
    var token: String
    var ownAvatar: String
    var ownUsername: String
    var emojiLastCachedTimestamp: String
    var lastSelectedLanguageName: String
    var lastSelectedLanguageCode: String
}
