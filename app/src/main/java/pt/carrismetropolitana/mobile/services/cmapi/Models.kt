package pt.carrismetropolitana.mobile.services.cmapi

//import kotlinx.serialization.SerialName
//import kotlinx.serialization.Serializable

//// @Serializable
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
//// @Serializable
data class Line(
    val id: String,
    val shortName: String,
    val longName: String,
    val color: String,
    val textColor: String,
    val routes: List<String>,
    val patterns: List<String>,
    val municipalities: List<String>,
    val localities: List<String?>,
    val facilities: List<Facility>
)

//// @Serializable
data class Route(
    val id: String,
    val lineId: String,
    val shortName: String,
    val longName: String,
    val color: String,
    val textColor: String,
    val patterns: List<String>,
    val municipalities: List<String>,
    val localities: List<String?>,
    val facilities: List<Facility>
)


//// @Serializable
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

//// @Serializable
data class Pattern(
    val id: String,
    val lineId: String,
    val routeId: String,
    val shortName: String,
    val direction: Int,
    val headsign: String,
    val color: String,
    val textColor: String,
    val validOn: List<String>,
    val municipalities: List<String>,
    val localities: List<String?>,
    val facilities: List<String>,
    val shapeId: String,
    val path: List<PathEntry>,
    val trips: List<Trip>
)

//// @Serializable
data class PathEntry(
    val stop: Stop,
    val stopSequence: Int,
    val allowPickup: Boolean,
    val allowDropOff: Boolean,
    val distanceDelta: Double
)

//// @Serializable
data class ScheduleEntry(
    val stopId: String,
    val stopSequence: Int,
    val arrivalTime: String,
    val arrivalTimeOperation: String
)

//// @Serializable
data class Trip(
    val id: String?,
    val calendarId: String,
    val calendarDescription: String,
    val dates: List<String>,
    val schedule: List<ScheduleEntry>
)

data class RealtimeETA(
    val lineId: String,
    val patternId: String,
    val routeId: String,
    val tripId: String,
    val headsign: String,
    val stopSequence: Int,
    val scheduledArrival: String?,
    val scheduledArrivalUnix: Int?,
    val estimatedArrival: String?,
    val estimatedArrivalUnix: Int?,
    val observedArrival: String?,
    val observedArrivalUnix: Int?,
    val vehicleId: String?
)

// @Serializable
data class GtfsRt(
    val header: Header,
    val entity: List<GtfsRtAlertEntity>
) {
    // @Serializable
    data class Header(
        val gtfsRealtimeVersion: String,
        val incrementality: String,
        val timestamp: Int
    )
}

// @Serializable
data class GtfsRtAlertEntity(
    val id: String,
    val alert: GtfsRtAlert
) {
    // @Serializable
    data class GtfsRtAlert(
        val activePeriod: List<ActivePeriod>,
        val cause: Cause,
        val descriptionText: TranslatedString,
        val effect: Effect,
        val headerText: TranslatedString,
        val informedEntity: List<EntitySelector>,
        val url: TranslatedString,
        val image: TranslatedImage
    ) {
        // @Serializable
        data class ActivePeriod(
            val start: Int,
            val end: Int
        )

        // @Serializable
        data class TranslatedString(
            val translation: List<Translation>
        ) {
            // @Serializable
            data class Translation(
                val text: String,
                val language: String
            )
        }

        // @Serializable
        data class TranslatedImage(
            val localizedImage: List<LocalizedImage>
        ) {
            // @Serializable
            data class LocalizedImage(
                val url: String,
                val mediaType: String,
                val language: String
            )
        }

        // @Serializable
        data class EntitySelector(
            val agencyId: String? = null,
            val routeId: String? = null,
            val routeType: Int? = null,
            val directionId: Int? = null,
            val stopId: String? = null
        )

        // @Serializable
        enum class Cause {
            UNKNOWN_CAUSE, OTHER_CAUSE, TECHNICAL_PROBLEM, STRIKE, DEMONSTRATION, ACCIDENT, HOLIDAY, WEATHER, MAINTENANCE, CONSTRUCTION, POLICE_ACTIVITY, MEDICAL_EMERGENCY
        }

        // @Serializable
        enum class Effect {
            NO_SERVICE, REDUCED_SERVICE, SIGNIFICANT_DELAYS, DETOUR, ADDITIONAL_SERVICE, MODIFIED_SERVICE, OTHER_EFFECT, UNKNOWN_EFFECT, STOP_MOVED, NO_EFFECT, ACCESSIBILITY_ISSUE
        }
    }
}

//// @Serializable
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