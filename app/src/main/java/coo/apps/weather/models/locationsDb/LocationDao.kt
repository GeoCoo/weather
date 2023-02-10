package coo.apps.weather.models.locationsDb

import androidx.room.*

@Dao
interface LocationDao {
    @Query("Select * From LocationRoom")
    fun getAll(): List<LocationRoom?> = listOf(
        LocationRoom(1, "Serres", 41.05, 23.32),
        LocationRoom(2, "Thess", 40.62, 22.94)
    )

    @Insert
    fun insertNewLocation(location: LocationRoom)

    @Update
    fun updateLocation(locationRoom: LocationRoom)

    @Delete
    fun deleteLocation(location: LocationRoom)
}