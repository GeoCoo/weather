package coo.apps.meteoray.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coo.apps.meteoray.databinding.LocationItemPageBinding
import coo.apps.meteoray.locationsDb.LocationEntity

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