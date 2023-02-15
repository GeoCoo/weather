package coo.apps.weather.locationsDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Types.ROWID

data class Location(
    val locationName: String?,
    val locationLat: Double,
    val locationLon: Double
)

@Entity
data class LocationRoom(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "location_id")
    val uid: Int,
    @ColumnInfo(name = "location_name")
    val locationName: String?,
    @ColumnInfo(name = "location_lat")
    val locationLat: Double,
    @ColumnInfo(name = "location_lon")
    val locationLon: Double
)