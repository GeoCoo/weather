package coo.apps.weather.locationsDb

import android.app.Application
import android.content.Context
import android.os.AsyncTask

class LocationsRepository(application: Application) {


    private var db: LocationDao = AppDatabase.getInstance(application)?.locationDao()!!


    //Fetch All the Users
    fun getAllLocations(): List<LocationRoom?> {
        return db.getAll()
    }

    // Insert new user
    fun insertUser(location: LocationRoom) {
        insertAsyncTask(db).execute(location)
    }

    // update user
    fun updateUser(location: LocationRoom) {
        db.updateLocation(location)
    }

    // Delete user
    fun deleteUser(location: LocationRoom) {
        db.deleteLocation(location)
    }

    private class insertAsyncTask internal constructor(private val locationDao: LocationDao) :
        AsyncTask<LocationRoom, Void, Void>() {


        override fun doInBackground(vararg params: LocationRoom): Void? {
            locationDao.insertNewLocation(params[0])
            return null
        }
    }

}