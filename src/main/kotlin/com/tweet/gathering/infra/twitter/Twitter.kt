package com.tweet.gathering.infra.twitter

import com.tweet.gathering.domain.service.TwitterAppSettings
import com.tweet.gathering.domain.service.TwitterToken
import com.tweet.gathering.infra.twitter.EncodeUtils.computeSignature
import com.tweet.gathering.infra.twitter.EncodeUtils.encode
import org.springframework.util.StringUtils
import java.util.*

object Twitter {

    private val SIGNATURE_METHOD :String = "HMAC-SHA1"

    private val AUTHORIZATION_VERIFY_CREDENTIALS = "OAuth " +
            "oauth_consumer_key=\"{key}\", " +
            "oauth_signature_method=\"" + SIGNATURE_METHOD + "\", " +
            "oauth_timestamp=\"{ts}\", " +
            "oauth_nonce=\"{nonce}\", " +
            "oauth_version=\"1.0\", " +
            "oauth_signature=\"{signature}\", " +
            "oauth_token=\"{token}\""

    fun buildAutHeader(appSettings: TwitterAppSettings, twitterToken: TwitterToken, method: String, url: String, query: String): String {
        val ts:String = ""+Date().time/1000
        val nounce:String = UUID.randomUUID().toString().replace("-".toRegex(),"")
        val parameters = "oauth_consumer_key=${appSettings.consumerKey}&oauth_nonce=$nounce&oauth_signature_method=$SIGNATURE_METHOD&oauth_timestamp=$ts&oauth_token=${encode(twitterToken.accessToken)}&oauth_version=1.0&track=${encode(query)}"
        val signature = "$method&" + encode(url) + "&" + encode(parameters)
        var result = AUTHORIZATION_VERIFY_CREDENTIALS
        result = StringUtils.replace(result, "{nonce}", nounce)
        result = StringUtils.replace(result, "{ts}", "" + ts)
        result = StringUtils.replace(result, "{key}", appSettings.consumerKey)
        result = StringUtils.replace(result, "{signature}", encode(computeSignature(signature, "${appSettings.consumerSecret}&${encode(twitterToken.accessTokenSecret)}")))
        result = StringUtils.replace(result, "{token}", encode(twitterToken.accessToken))
        return result
    }

}