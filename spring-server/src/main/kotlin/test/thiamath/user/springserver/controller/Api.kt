package test.thiamath.user.springserver.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Api {

    @GetMapping
    fun test(): String = "Hello API"
}