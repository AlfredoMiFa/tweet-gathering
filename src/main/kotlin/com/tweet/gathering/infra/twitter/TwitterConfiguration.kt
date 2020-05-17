package com.tweet.gathering.infra.twitter

import com.tweet.gathering.domain.service.TwitterAppSettings
import com.tweet.gathering.domain.service.TwitterToken
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class TwitterConfiguration(@Value("\${twitter.consumer-key}") private val consumerKey:String,
                                @Value("\${twitter.consumer-secret}") private val consumerSecret:String,
                                @Value("\${twitter.access-token}") private val accessToken:String,
                                @Value("\${twitter.access-token-secret}") private val accessTokenSecret: String) {

    @Bean
    open fun twitterAppSetting(): TwitterAppSettings {
        return TwitterAppSettings(consumerKey, consumerSecret)
    }

    @Bean
    open fun twitterToken(): TwitterToken {
        return TwitterToken(accessToken, accessTokenSecret)
    }

}