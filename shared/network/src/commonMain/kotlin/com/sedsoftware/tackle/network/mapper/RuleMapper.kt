package com.sedsoftware.tackle.network.mapper

import com.sedsoftware.tackle.domain.model.Rule
import com.sedsoftware.tackle.network.response.RuleItemResponse

internal object RuleMapper {

    fun map(from: RuleItemResponse): Rule =
        Rule(
            id = from.id,
            text = from.text,
            hint = from.hint
        )
}
