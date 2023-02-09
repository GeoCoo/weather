package coo.apps.weather.models.LocationsDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao{
    @Query("Select * From LocationRoom")
    fun getAll(): List<LocationRoom>?

    @Insert
    fun insertNewLocation(location:LocationRoom)

    @Delete
    fun deleteLocation(location: LocationRoom)
}