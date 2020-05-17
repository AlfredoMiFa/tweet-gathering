package com.tweet.gathering.domain.service

import com.tweet.gathering.domain.TrackedHashTag
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@Service
class TwitterGatherRunner(private val gatherService: TweetGatherService, private val rabbitTemplate: RabbitTemplate) {

    @RabbitListener(queues = ["twitter-track-hashtag"])
    fun recive(hashTag: TrackedHashTag) {
        val streamFrom = this.gatherService.streamFrom(hashTag.hashTag).filter({
            return@filter it.id.isNotEmpty() && it.text.isNotEmpty() && it.createdAt.isNotEmpty()
        })
        val suscribe = streamFrom.subscribe({
            println(it.text)
            Mono.fromFuture(CompletableFuture.runAsync{
                this.rabbitTemplate.convertAndSend("twitter-exchange","track.${hashTag.queue}",it)
            })
        })
        Schedulers.elastic().schedule({
            suscribe.dispose()
        },10L,TimeUnit.SECONDS)
    }

}