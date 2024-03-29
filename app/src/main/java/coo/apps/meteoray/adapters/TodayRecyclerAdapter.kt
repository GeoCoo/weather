package coo.apps.meteoray.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coo.apps.meteoray.R
import coo.apps.meteoray.databinding.MainRecyclerItemBinding
import coo.apps.meteoray.models.main.DayTable
import coo.apps.meteoray.models.main.getSmallIcons


class TodayRecyclerAdapter(private val list: List<DayTable>) :
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
                    item.temp + this@DailyViewHolder.itemView.context.getString(R.string.celcius_symbol)
                this.icon.setImageResource(getSmallIcons(item.icon))
                this.windDirection.text = item.wind + item.dirname
                this.windIndicator.rotation = item.dir?.toFloat()!!
            }
        }
    }
}