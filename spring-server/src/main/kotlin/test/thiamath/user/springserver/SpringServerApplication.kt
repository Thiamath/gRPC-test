package test.thiamath.user.springserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringServerApplication

fun main(args: Array<String>) {
    runApplication<SpringServerApplication>(*args)
}
