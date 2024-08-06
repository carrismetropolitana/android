package pt.carrismetropolitana.mobile.services.cmapi

//import kotlinx.serialization.SerialName
//import kotlinx.serialization.Serializable

//@Serializable
enum class Facility {
    School,
    Boat,
    Subway,
    Train,
    Hospital,
    Shopping,
    TransitOffice,
    LightRail,
    BikeSharing
}

//@Serializable
data class Stop(
    val id: String,
    val name: String,
    val shortName: String? = null,
    val ttsName: String? = null,
    val operationalStatus: String? = null,
    val lat: String,
    val lon: String,
    val locality: String? = null,
    val parishId: String? = null,
    val parishName: String? = null,
    val municipalityId: String,
    val municipalityName: String,
    val districtId: String,
    val districtName: String,
    val regionId: String,
    val regionName: String,
    val wheelchairBoarding: String? = null,
    val facilities: List<Facility>,
    val lines: List<String>? = null,
    val routes: List<String>? = null,
    val patterns: List<String>? = null
)

//@Serializable
data class ENCM(
    val id: String,
    val name: String,
    val lat: String,
    val lon: String,
    val phone: String,
    val email: String,
    val url: String,
    val address: String,
    val postalCode: String,
    val locality: String,
    val parishId: String,
    val parishName: String,
    val municipalityId: String,
    val municipalityName: String,
    val districtId: String,
    val districtName: String,
    val regionId: String,
    val regionName: String,
    val hoursMonday: List<String>,
    val hoursTuesday: List<String>,
    val hoursWednesday: List<String>,
    val hoursThursday: List<String>,
    val hoursFriday: List<String>,
    val hoursSaturday: List<String>,
    val hoursSunday: List<String>,
    val hoursSpecial: String,
    val stops: List<String>,
    val currentlyWaiting: Int,
    val expectedWaitTime: Int,
    val activeCounters: Int,
    val isOpen: Boolean
)