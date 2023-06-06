package coo.apps.meteoray.locationsDb

import android.app.Application
import android.os.AsyncTask

class LocationsRepository(application: Application) {

    private var db: LocationDao = AppDatabase.getInstance(application)?.locationDao()!!

    fun getAllLocations(): List<LocationEntity?> {
        return db.getAll()
    }

    fun getSingleLocation(id: Int): LocationEntity {
        return db.getSingleLocation(id)
    }

    fun insertNewLocation(location: LocationEntity?) {
        InsertAsyncTask(db).execute(location)
    }

    fun updateLocation(location: LocationEntity?) {
        UpdateAsyncTask(db).execute(location)
    }

    fun deleteLocation(location: LocationEntity) {
        db.deleteLocation(location)
    }

    private class UpdateAsyncTask(private val locationDao: LocationDao) :
        AsyncTask<LocationEntity, Void, Void>() {

        override fun doInBackground(vararg params: LocationEntity): Void? {
            locationDao.updateLocation(params[0])
            return null
        }
    }

    private class InsertAsyncTask(private val locationDao: LocationDao) :
        AsyncTask<LocationEntity, Void, Void>() {

        override fun doInBackground(vararg params: LocationEntity): Void? {
            locationDao.insertNewLocation(params[0])
            return null
        }
    }

}