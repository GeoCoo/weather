package coo.apps.meteoray.locationsDb

import androidx.room.*

@Dao
interface LocationDao {
    @Query("Select * From LocationEntity")
    fun getAll(): List<LocationEntity?>

    @Query("SELECT * FROM LocationEntity where location_id = :id")
    fun getSingleLocation(id: Int): LocationEntity

    @Insert
    fun insertNewLocation(location: LocationEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateLocation(locationEntity: LocationEntity)

    @Delete
    fun deleteLocation(location: LocationEntity)
}