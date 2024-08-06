package pt.carrismetropolitana.mobile.services.cmapi
//
//import io.ktor.client.HttpClient
//import io.ktor.client.call.body
//import io.ktor.client.request.get
//import io.ktor.util.network.UnresolvedAddressException
//import kotlinx.serialization.SerializationException
//
//class CMAPIClient(
//    private val httpClient: HttpClient
//) {
//    suspend fun getStops(): List<Stop> {
//        val response = try {
//            httpClient.get("https://api.carrismetropolitana.pt/stops")
//        } catch (e: UnresolvedAddressException) {
//            return emptyList()
//        } catch (e: SerializationException) {
//            return emptyList()
//        }
//
//        return when (response.status.value) {
//            in 200..299 -> {
//                response.body<List<Stop>>()
//            }
//            else -> {
//                emptyList()
//            }
//        }
//    }
//}