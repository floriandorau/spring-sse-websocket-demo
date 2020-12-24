package dev.dorau.ssesocketdemo.sse

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import java.time.Duration
import java.time.LocalTime


@RestController
@RequestMapping("/sse-server")
class SSEController {

    @GetMapping
    fun foo(): ResponseEntity<String>? {
        return ResponseEntity.ok("Server Sent Events")
    }

    @GetMapping(path = ["/stream-flux"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun streamFlux(): Flux<String> {
        return Flux.interval(Duration.ofSeconds(1))
            .map { sequence: Long? -> "Flux - " + LocalTime.now().toString() }
    }

    @GetMapping("/stream-sse")
    fun streamEvents(): Flux<ServerSentEvent<String>>? {
        return Flux.interval(Duration.ofSeconds(1))
            .map { sequence: Long ->
                ServerSentEvent.builder<String>()
                    .id(sequence.toString())
                    .event("periodic-event")
                    .data("SSE - " + LocalTime.now().toString())
                    .comment("SSE - Comment")
                    .build()
            }
    }
}
