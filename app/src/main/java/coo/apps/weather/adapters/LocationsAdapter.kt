package coo.apps.weather.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coo.apps.weather.databinding.LocationRecyclerItemBinding
import coo.apps.weather.models.locationsDb.LocationRoom


class LocationsAdapter(private val list: List<LocationRoom?>) :
    RecyclerView.Adapter<LocationsAdapter.LocationsViewHolder>() {

    private lateinit var binding: LocationRecyclerItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        binding =
            LocationRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        val location = list[position]
        holder.bind(location)
    }

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemCount(): Int = list.size

    inner class LocationsViewHolder(private var binding: LocationRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LocationRoom?) {
            binding.apply {
                this.locationName.text = item?.locationName

            }
        }
    }
}