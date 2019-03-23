package com.example.helse.data.database
import androidx.room.TypeConverter
import com.example.helse.data.entities.Location

object LocationConverter {

    @TypeConverter
    fun fromLocation(location: Location?): String? {
        return if (location == null) null else String.format("%s,%s,%f,%f,%s",
            location.location,
            location.superlocation,
            location.longitude,
            location.latitude,
            location.stationID
        )

    }

    @TypeConverter
    fun toLocation(location: String?): Location? {
        return if (location == null) null else {
            val attributes = location.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            return Location(
                attributes[0],
                attributes[1],
                java.lang.Double.parseDouble(attributes[2]),
                java.lang.Double.parseDouble(attributes[3]),
                attributes[4]
            )
        }
    }


}
