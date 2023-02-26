package coo.apps.meteoray.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import coo.apps.meteoray.databinding.PlacesItemBinding

class PlacesResultAdapter(
    private var placesClient: PlacesClient,
    val onClick: (prediction: AutocompletePrediction) -> Unit,
) :
    RecyclerView.Adapter<PlacesResultAdapter.ViewHolder>(), Filterable {

    private var mResultList: ArrayList<AutocompletePrediction>? = arrayListOf()
    private lateinit var binding: PlacesItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = PlacesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mResultList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = mResultList?.get(position)
        location?.let { holder.onBind(it) }
    }

    inner class ViewHolder(private var binding: PlacesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(prediction: AutocompletePrediction) {
            binding.apply {
                this.placeTitle.text = prediction?.getPrimaryText(null)
                this.placeSubTitle.text = prediction?.getSecondaryText(null)
            }
            binding.placeElement.setOnClickListener {
                onClick.invoke(prediction)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val results = FilterResults()
                mResultList = getPredictions(constraint)
                if (mResultList != null) {
                    results.values = mResultList
                    results.count = mResultList!!.size
                    notifyDataSetChanged()
                }
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {

            }
        }
    }

    private fun getPredictions(constraint: CharSequence): ArrayList<AutocompletePrediction>? {
        val result: ArrayList<AutocompletePrediction> = arrayListOf()
        val token = AutocompleteSessionToken.newInstance()
        val request =
            FindAutocompletePredictionsRequest.builder()
                .setSessionToken(token)
                .setQuery(constraint.toString())
                .build()

        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                result.addAll(response.autocompletePredictions)
            }.addOnFailureListener { exception: Exception? ->
                if (exception is ApiException) {
                    Log.e("TAG", "Place not found: " + exception.statusCode)
                    //mContext.showToastLong("Place not found")
                }
            }
        return result
    }
}