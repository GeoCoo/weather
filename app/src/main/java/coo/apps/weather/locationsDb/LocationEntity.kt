package coo.apps.weather.locationsDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "location_id")
    val uid: Int?=null,
    @ColumnInfo(name = "location_name")
    val locationName: String?,
    @ColumnInfo(name = "location_lat")
    val locationLat: Double,
    @ColumnInfo(name = "location_lon")
    val locationLon: Double
)