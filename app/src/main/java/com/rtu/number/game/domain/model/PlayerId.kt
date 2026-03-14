package com.rtu.number.game.domain.model

enum class PlayerId {
    FIRST,
    SECOND,
}

fun PlayerId.opponent(): PlayerId {
    return if (this == PlayerId.FIRST) {
        PlayerId.SECOND
    } else {
        PlayerId.FIRST
    }
}
