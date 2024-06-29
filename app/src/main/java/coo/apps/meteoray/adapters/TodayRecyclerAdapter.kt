package coo.apps.meteoray.adapters


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coo.apps.meteoray.R
import coo.apps.meteoray.databinding.MainRecyclerItemBinding
import coo.apps.meteoray.models.main.DayTable
import coo.apps.meteoray.models.main.getSmallIcons


class TodayRecyclerAdapter(
    private val list: List<DayTable>, private val hasBofor: Boolean,
    private val hasFahreneit: Boolean
) :
    RecyclerView.Adapter<TodayRecyclerAdapter.DailyViewHolder>() {


    private lateinit var binding: MainRecyclerItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        binding =
            MainRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val question = list[position]
        holder.bind(question)
    }

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemCount(): Int = list.size

    inner class DailyViewHolder(private var binding: MainRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DayTable) {
            binding.apply {
                this.dateTime.text = item.time
                this.highest.text =
                    if (hasFahreneit.not()) item.temp + this@DailyViewHolder.itemView.context.getString(
                        R.string.celcius_symbol
                    ) else item.tempfrt + this@DailyViewHolder.itemView.context.getString(R.string.fahreneit_symbol)
                this.icon.setImageResource(getSmallIcons(item.icon))
                this.windDirection.text =
                    if (hasBofor.not()) item.wind + item.dirname else item.windbft + item.dirname
                this.windIndicator.rotation = item.dir?.toFloat()!!
                this.heat.setColorFilter(Color.parseColor(item.heatRisk))
                this.rain.setColorFilter(Color.parseColor(item.rainRisk))
                this.frost.setColorFilter(Color.parseColor(item.frostRisk))
                this.wind.setColorFilter(Color.parseColor(item.windRisk))
            }
        }
    }
}