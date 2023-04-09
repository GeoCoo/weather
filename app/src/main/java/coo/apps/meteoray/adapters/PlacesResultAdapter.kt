package coo.apps.meteoray.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.model.AutocompletePrediction
import coo.apps.meteoray.databinding.PlacesItemBinding

class PlacesResultAdapter(val onClick: (prediction: AutocompletePrediction) -> Unit) :
    RecyclerView.Adapter<PlacesResultAdapter.ViewHolder>() {
    private lateinit var binding: PlacesItemBinding

    private var placesList: ArrayList<AutocompletePrediction>? = arrayListOf()

    fun filterList(filterlist: ArrayList<AutocompletePrediction>?) {
        placesList = filterlist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = PlacesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return placesList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = placesList?.get(position)
        location?.let { holder.onBind(it) }
    }

    inner class ViewHolder(private var binding: PlacesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(prediction: AutocompletePrediction) {
            binding.apply {
                this.placeTitle.text = prediction.getPrimaryText(null)
                this.placeSubTitle.text = prediction.getSecondaryText(null)
            }
            binding.placeElement.setOnClickListener {
                onClick.invoke(prediction)
            }
        }
    }
}