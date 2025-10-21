package com.sedsoftware.tackle.compose.core

import com.sedsoftware.tackle.domain.TackleException
import org.jetbrains.compose.resources.getString
import tackle.shared.compose.generated.resources.Res
import tackle.shared.compose.generated.resources.common_error_attachment_type
import tackle.shared.compose.generated.resources.common_error_attachments_limit
import tackle.shared.compose.generated.resources.common_error_file_not_available
import tackle.shared.compose.generated.resources.common_error_file_size_limit
import tackle.shared.compose.generated.resources.common_error_file_type_unknown
import tackle.shared.compose.generated.resources.common_error_no_internet
import tackle.shared.compose.generated.resources.common_error_schedule_date
import tackle.shared.compose.generated.resources.common_error_serialization
import tackle.shared.compose.generated.resources.common_error_server
import tackle.shared.compose.generated.resources.common_error_unknown

internal suspend fun exceptionToString(e: TackleException): String = when (e) {
    is TackleException.NetworkException -> getString(Res.string.common_error_no_internet)
    is TackleException.RemoteServerException -> "${getString(Res.string.common_error_server)}: ${e.code}"
    is TackleException.SerializationException -> getString(Res.string.common_error_serialization)
    is TackleException.FileSizeExceeded -> getString(Res.string.common_error_file_size_limit)
    is TackleException.FileTypeNotSupported -> getString(Res.string.common_error_file_type_unknown)
    is TackleException.FileNotAvailable -> getString(Res.string.common_error_file_not_available)
    is TackleException.AttachmentsLimitExceeded -> "${getString(Res.string.common_error_attachments_limit)}: ${e.limit}"
    is TackleException.ScheduleDateTooShort -> getString(Res.string.common_error_schedule_date)
    is TackleException.AttachmentDifferentType -> getString(Res.string.common_error_attachment_type)
    is TackleException.DownloadableFileEmpty -> getString(Res.string.common_error_attachment_type)
    is TackleException.Unknown -> getString(Res.string.common_error_unknown)
    is TackleException.MissedRegistrationData -> ""
}
