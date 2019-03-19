package com.example.helse

import com.example.helse.data.entities.Airquality
import com.example.helse.data.entities.AirqualityForecast
import com.example.helse.data.entities.AirqualityVariables
import com.example.helse.data.entities.Location

val alnabruLocation = Location(
    "Alnabru",
    "Oslo",
    59.92767,
    10.84655,
    "NO0057A"
)

val goodAirqualitySampleResponse =
    "{\"data\": {\"time\": [{\"variables\": {\"no2_local_fraction_industry\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_concentration\": {\"units\": \"ug/m3\", \"value\": 1.0733610391616821}, \"o3_concentration\": {\"units\": \"ug/m3\", \"value\": 68.15038299560547}, \"AQI_no2\": {\"units\": \"1\", \"value\": 1.0}, \"pm10_local_fraction_traffic_exhaust\": {\"units\": \"%\", \"value\": 5.0}, \"no2_local_fraction_heating\": {\"units\": \"%\", \"value\": 0.0}, \"pm10_local_fraction_heating\": {\"units\": \"%\", \"value\": 8.0}, \"AQI_o3\": {\"units\": \"1\", \"value\": 1.6}, \"o3_nonlocal_fraction\": {\"units\": \"%\", \"value\": 100.0}, \"pm25_local_fraction_heating\": {\"units\": \"%\", \"value\": 11.0}, \"no2_nonlocal_fraction\": {\"units\": \"%\", \"value\": 32.0}, \"pm10_local_fraction_traffic_nonexhaust\": {\"units\": \"%\", \"value\": 0.0}, \"AQI\": {\"units\": \"1\", \"value\": 1.6}, \"pm25_local_fraction_traffic_nonexhaust\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_local_fraction_shipping\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_local_fraction_industry\": {\"units\": \"%\", \"value\": 0.0}, \"no2_local_fraction_traffic_exhaust\": {\"units\": \"%\", \"value\": 66.0}, \"pm10_concentration\": {\"units\": \"ug/m3\", \"value\": 1.5612730979919434}, \"no2_concentration\": {\"units\": \"ug/m3\", \"value\": 3.354196071624756}, \"AQI_pm10\": {\"units\": \"1\", \"value\": 1.0}, \"pm10_local_fraction_industry\": {\"units\": \"%\", \"value\": 0.0}, \"pm10_local_fraction_shipping\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_local_fraction_traffic_exhaust\": {\"units\": \"%\", \"value\": 7.0}, \"pm10_nonlocal_fraction\": {\"units\": \"%\", \"value\": 85.0}, \"AQI_pm25\": {\"units\": \"1\", \"value\": 1.0}, \"no2_local_fraction_shipping\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_nonlocal_fraction\": {\"units\": \"%\", \"value\": 80.0}}, \"to\": \"2019-03-18T01:00:00Z\", \"from\": \"2019-03-18T01:00:00Z\"}]}, \"meta\": {\"superlocation\": {\"areaclass\": \"kommune\", \"areacode\": \"0301\", \"superareacode\": \"03\", \"name\": \"Oslo\", \"longitude\": \"10.84655\", \"latitude\": \"59.92767\"}, \"reftime\": \"2019-03-18T12:00:00Z\", \"sublocations\": [], \"location\": {\"name\": \"Alnabru\", \"areacode\": \"NO0057A\", \"latitude\": \"10.84655\", \"longitude\": \"59.92767\"}}}"

// The insane pm25_concentration is what makes it bad
val badAirqualitySampleResponse =
    "{\"data\": {\"time\": [{\"variables\": {\"no2_local_fraction_industry\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_concentration\": {\"units\": \"ug/m3\", \"value\": 19321.0391616821}, \"o3_concentration\": {\"units\": \"ug/m3\", \"value\": 68.15038299560547}, \"AQI_no2\": {\"units\": \"1\", \"value\": 1.0}, \"pm10_local_fraction_traffic_exhaust\": {\"units\": \"%\", \"value\": 5.0}, \"no2_local_fraction_heating\": {\"units\": \"%\", \"value\": 0.0}, \"pm10_local_fraction_heating\": {\"units\": \"%\", \"value\": 8.0}, \"AQI_o3\": {\"units\": \"1\", \"value\": 1.6}, \"o3_nonlocal_fraction\": {\"units\": \"%\", \"value\": 100.0}, \"pm25_local_fraction_heating\": {\"units\": \"%\", \"value\": 11.0}, \"no2_nonlocal_fraction\": {\"units\": \"%\", \"value\": 32.0}, \"pm10_local_fraction_traffic_nonexhaust\": {\"units\": \"%\", \"value\": 0.0}, \"AQI\": {\"units\": \"1\", \"value\": 1.6}, \"pm25_local_fraction_traffic_nonexhaust\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_local_fraction_shipping\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_local_fraction_industry\": {\"units\": \"%\", \"value\": 0.0}, \"no2_local_fraction_traffic_exhaust\": {\"units\": \"%\", \"value\": 66.0}, \"pm10_concentration\": {\"units\": \"ug/m3\", \"value\": 1.5612730979919434}, \"no2_concentration\": {\"units\": \"ug/m3\", \"value\": 3.354196071624756}, \"AQI_pm10\": {\"units\": \"1\", \"value\": 1.0}, \"pm10_local_fraction_industry\": {\"units\": \"%\", \"value\": 0.0}, \"pm10_local_fraction_shipping\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_local_fraction_traffic_exhaust\": {\"units\": \"%\", \"value\": 7.0}, \"pm10_nonlocal_fraction\": {\"units\": \"%\", \"value\": 85.0}, \"AQI_pm25\": {\"units\": \"1\", \"value\": 1.0}, \"no2_local_fraction_shipping\": {\"units\": \"%\", \"value\": 0.0}, \"pm25_nonlocal_fraction\": {\"units\": \"%\", \"value\": 80.0}}, \"to\": \"2019-03-18T01:00:00Z\", \"from\": \"2019-03-18T01:00:00Z\"}]}, \"meta\": {\"superlocation\": {\"areaclass\": \"kommune\", \"areacode\": \"0301\", \"superareacode\": \"03\", \"name\": \"Oslo\", \"longitude\": \"10.84655\", \"latitude\": \"59.92767\"}, \"reftime\": \"2019-03-18T12:00:00Z\", \"sublocations\": [], \"location\": {\"name\": \"Alnabru\", \"areacode\": \"NO0057A\", \"latitude\": \"10.84655\", \"longitude\": \"59.92767\"}}}"

val wrongResponse = "{\"message\": \"undefined station requested\"}"

val parsedAirqualityResponse = AirqualityForecast(
    alnabruLocation,
    Airquality(
        "2019-03-18T01:00:00Z",
        "2019-03-18T01:00:00Z",
        AirqualityVariables(
            68.15038,
            1.5612731,
            1.073361,
            3.354196
        )
    )
)

val goodLocationResponse =
    "[{\"delomrade\": {\"name\": \"Alfaset\", \"areacode\": \"03013700\"}, \"longitude\": \"10.84655\", \"name\": \"Alnabru\", \"eoi\": \"NO0057A\", \"height\": 3.0, \"grunnkrets\": {\"name\": \"Alfaset\", \"areacode\": \"03013701\"}, \"kommune\": {\"name\": \"Oslo\", \"areacode\": \"0301\"}, \"latitude\": \"59.92767\"}, {\"delomrade\": {\"name\": \"Sarpsborg vest\", \"areacode\": \"01050100\"}, \"longitude\": \"11.08919\", \"name\": \"Alvim\", \"eoi\": \"NO0110A\", \"height\": 3.0, \"grunnkrets\": {\"name\": \"Alvimhaugen\", \"areacode\": \"01050102\"}, \"kommune\": {\"name\": \"Sarpsborg\", \"areacode\": \"0105\"}, \"latitude\": \"59.27378\"}]"

// The first name is what makes it bad
val badLocationResponse =
    "[{\"delomrade\": {\"name\": \"Alfaset\", \"areacode\": \"03013700\"}, \"longitude\": \"10.84655\", \"name\": \"HURRDURRLAND\", \"eoi\": \"NO0057A\", \"height\": 3.0, \"grunnkrets\": {\"name\": \"Alfaset\", \"areacode\": \"03013701\"}, \"kommune\": {\"name\": \"Oslo\", \"areacode\": \"0301\"}, \"latitude\": \"59.92767\"}, {\"delomrade\": {\"name\": \"Sarpsborg vest\", \"areacode\": \"01050100\"}, \"longitude\": \"11.08919\", \"name\": \"Alvim\", \"eoi\": \"NO0110A\", \"height\": 3.0, \"grunnkrets\": {\"name\": \"Alvimhaugen\", \"areacode\": \"01050102\"}, \"kommune\": {\"name\": \"Sarpsborg\", \"areacode\": \"0105\"}, \"latitude\": \"59.27378\"}]"

val parsedLocationResponse = mutableListOf(
    Location("Alnabru", "Oslo", 10.84655, 59.92767, "NO0057A"),
    Location("Alvim", "Sarpsborg", 11.08919, 59.27378, "NO0110A")
)


