package coo.apps.weather.utils


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coo.apps.weather.R
import coo.apps.weather.databinding.MainRecyclerItemBinding
import coo.apps.weather.models.main.Overview
import coo.apps.weather.models.main.getSmallIcons
import coo.apps.weather.network.convertDate


class DailyRecyclerAdapter(private val list: List<Overview>) : RecyclerView.Adapter<DailyRecyclerAdapter.TodayViewHolder>() {


    private lateinit var binding: MainRecyclerItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayViewHolder {
        binding = MainRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodayViewHolder, position: Int) {
        val question = list[position]
        holder.bind(question, position)
    }

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemCount(): Int = list.size

    inner class TodayViewHolder(private var binding: MainRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Overview, position: Int) {
            binding.apply {
                this.dateTime.text = if (position != 0) convertDate(item.date) else this@TodayViewHolder.itemView.context.getString(R.string.today)
                this.highest.text = item.tempmax + this@TodayViewHolder.itemView.context.getString(R.string.celcius_symbol)
                this.lowest.text = item.tempmin + this@TodayViewHolder.itemView.context.getString(R.string.celcius_symbol)
                this.icon.setImageResource(getSmallIcons(item.icon))
                this.windDirection.text = item.windspeed.toString() + item.winddirname
            }
        }
    }
}