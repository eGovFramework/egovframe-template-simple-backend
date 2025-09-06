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
    log.debug{"##### EgovBootApplication Start #####"}
    // runApplication 함수 하나로 애플리케이션 실행을 통일하고, 람다 블록으로 추가 설정을 전달합니다.
    runApplication<EgovBootApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
    log.debug{"##### EgovBootApplication End #####"}
}