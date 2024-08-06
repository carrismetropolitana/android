package pt.carrismetropolitana.mobile.services.cmapi
//
//import android.util.Log
//import io.ktor.client.HttpClient
////import io.ktor.client.engine.android.Android
//import io.ktor.client.plugins.DefaultRequest
//import io.ktor.client.plugins.HttpTimeout
//import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
//import io.ktor.client.plugins.defaultRequest
//import io.ktor.client.plugins.logging.LogLevel
//import io.ktor.client.plugins.logging.Logger
//import io.ktor.client.plugins.logging.Logging
//import io.ktor.client.request.accept
//import io.ktor.client.request.header
//import io.ktor.http.ContentType
//import io.ktor.http.HttpHeaders
//import io.ktor.serialization.kotlinx.json.json
//import kotlinx.serialization.json.Json

private const val TIMEOUT = 6_000L

//interface CMAPIService {
//    suspend fun getStops(): List<Stop>
//
//    companion object {
//        fun create(): CMAPIService {
//            return CMAPIServiceImpl(
//                client = HttpClient(Android) {
//                    install(Logging) {
//                        level = LogLevel.ALL
//                        logger = object : Logger {
//                            override fun log(message: String) {
//                                Log.v("[Logger::KTOR] — ", message)
//                            }
//                        }
//                    }
//
//                    install(ContentNegotiation) {
//                        json(
//                            Json {
//                                prettyPrint = true
//                                isLenient = true
//                                useAlternativeNames = true
//                                ignoreUnknownKeys = true
//                                encodeDefaults = false
//                            }
//                        )
//                    }
//
//                    install(HttpTimeout) {
//                        requestTimeoutMillis = TIMEOUT
//                        connectTimeoutMillis = TIMEOUT
//                        socketTimeoutMillis = TIMEOUT
//                    }
//
//                    defaultRequest {
//                        accept(ContentType.Application.Json)
//                    }
//                }
//            )
//        }
//    }
//}