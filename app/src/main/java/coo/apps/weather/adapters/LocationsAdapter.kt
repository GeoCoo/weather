package coo.apps.weather.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coo.apps.weather.databinding.LocationRecyclerItemBinding
import coo.apps.weather.models.LocationsDb.LocationRoom

class LocationsAdapter(private val list: List<LocationRoom>) :
    RecyclerView.Adapter<LocationsAdapter.LocationViewHolder>() {

    var onItemClick: ((LocationRoom) -> Unit)? = null

    private lateinit var binding: LocationRecyclerItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        binding = LocationRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationsAdapter.LocationViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemCount(): Int = list.size

    inner class LocationViewHolder(private var binding: LocationRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item:LocationRoom) {
            binding.apply {
                this.locationName.text = item.locationName
                onItemClick?.invoke(item)
            }
        }
    }
}