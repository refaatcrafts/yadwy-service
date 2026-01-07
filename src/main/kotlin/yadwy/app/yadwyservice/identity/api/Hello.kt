package yadwy.app.yadwyservice.identity.api

import app.yadwy.api.HelloApi
import app.yadwy.model.HelloResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class Hello : HelloApi {

    override fun sayHello(): ResponseEntity<HelloResponse> {
        return ResponseEntity.ok(HelloResponse("Hello World!", LocalDateTime.now()))
    }
}