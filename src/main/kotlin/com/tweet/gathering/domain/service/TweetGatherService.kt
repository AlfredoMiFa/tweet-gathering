package com.tweet.gathering.domain.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class TweetGatherService(private val twitterAppSettings: TwitterAppSettings, private val twitterToken: TwitterToken, private val webClient: WebClient) {



}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Tweet(val id:String="",val text:String="",@JsonProperty("created_at") val createdAt: String="", val user: TwitterUser = TwitterUser("",""))

@JsonIgnoreProperties(ignoreUnknown = true)
data class TwitterUser(val id:String, val name:String)