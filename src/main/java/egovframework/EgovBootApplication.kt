package egovframework

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan

@ServletComponentScan
@SpringBootApplication
class EgovBootApplication {
    companion object
}

private val log = KotlinLogging.logger {}

fun main(args: Array<String>) {
    log.debug { "##### EgovBootApplication Start #####" }
    runApplication<EgovBootApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
    log.debug { "##### EgovBootApplication End #####" }
}