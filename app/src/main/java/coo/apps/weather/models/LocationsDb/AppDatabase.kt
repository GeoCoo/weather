package coo.apps.weather.models.LocationsDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocationRoom::class], version = 1)
abstract class AppDatabase :RoomDatabase(){
    abstract fun locationDao():LocationDao
}
