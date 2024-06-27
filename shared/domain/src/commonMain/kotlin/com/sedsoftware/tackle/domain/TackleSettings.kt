package com.sedsoftware.tackle.domain

interface TackleSettings {
    var domain: String
    var clientId: String
    var clientSecret: String
    var token: String
    var ownAvatar: String
    var ownUsername: String
    var emojiLastCachedTimestamp: String
    var lastSelectedLanguageName: String
    var lastSelectedLanguageCode: String
}
