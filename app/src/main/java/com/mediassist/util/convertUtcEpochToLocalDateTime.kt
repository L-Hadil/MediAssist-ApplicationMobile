package com.mediassist.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Convertit un epoch-second UTC en LocalDateTime dans la timezone de l'appareil.
 */
fun convertUtcEpochToLocalDateTime(epochSeconds: Long): LocalDateTime {
    return Instant.ofEpochSecond(epochSeconds)
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()
}
