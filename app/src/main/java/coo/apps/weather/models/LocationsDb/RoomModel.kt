package coo.apps.weather.models.LocationsDb

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey


data class LocationRoom(
    @PrimaryKey val uid:Int,
    @ColumnInfo val locationName:String,
    @ColumnInfo val locationLat:Double,
    @ColumnInfo val locationLon: Double
)