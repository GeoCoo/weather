package coo.apps.weather.models.locationsDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationRoom(
    @PrimaryKey val uid: Int,
    @ColumnInfo val locationName: String,
    @ColumnInfo val locationLat: Double,
    @ColumnInfo val locationLon: Double
)