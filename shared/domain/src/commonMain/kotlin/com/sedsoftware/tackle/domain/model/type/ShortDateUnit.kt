package com.sedsoftware.tackle.domain.model.type

sealed class ShortDateUnit {
    data object Now : ShortDateUnit()
    data class Seconds(val value: Int) : ShortDateUnit()
    data class Minutes(val value: Int) : ShortDateUnit()
    data class Hours(val value: Int) : ShortDateUnit()
    data class Days(val value: Int) : ShortDateUnit()
    data class Months(val value: Int) : ShortDateUnit()
    data class Years(val value: Int) : ShortDateUnit()
}
