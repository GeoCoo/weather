package coo.apps.weather.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coo.apps.weather.databinding.LocationItemPageBinding
import coo.apps.weather.databinding.LocationRecyclerItemBinding
import coo.apps.weather.locationsDb.LocationEntity

class ViewPagerAdapter(
    private val locations: List<LocationEntity?>,
) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {

    private lateinit var binding: LocationItemPageBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        binding =
            LocationItemPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val location = locations[position]
        holder.bind(location)
    }

    override fun getItemCount(): Int = locations.size


    inner class ViewPagerViewHolder(private var binding: LocationItemPageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(itemLocationEntity: LocationEntity?){

        }

    }

}