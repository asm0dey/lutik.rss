package generator

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun pubDate(): String =
    ZonedDateTime.now(ZoneId.of("GMT")).format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss zzz"))

val <T : Any> T.logger: Logger
    get() = LoggerFactory.getLogger(this::class.java)
