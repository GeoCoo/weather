package coo.apps.meteoray.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coo.apps.meteoray.R
import coo.apps.meteoray.databinding.MainRecyclerItemBinding
import coo.apps.meteoray.models.main.Overview
import coo.apps.meteoray.models.main.getSmallIcons


class DailyRecyclerAdapter(
    private val list: List<Overview>,
    private val hasBofor: Boolean,
    private val hasFahreneit: Boolean
) :
    RecyclerView.Adapter<DailyRecyclerAdapter.TodayViewHolder>() {

    private lateinit var binding: MainRecyclerItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayViewHolder {
        binding =
            MainRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
                this.dateTime.text =
                    if (position != 0) item.name else this@TodayViewHolder.itemView.context.getString(
                        R.string.today
                    )
                this.highest.text =
                    if (hasFahreneit.not()) item.tempmax + this@TodayViewHolder.itemView.context.getString(
                        R.string.celcius_symbol
                    ) else item.tempMaxFrt + this@TodayViewHolder.itemView.context.getString(R.string.fahreneit_symbol)
                this.lowest.text =
                    if (hasFahreneit.not()) item.tempmin + this@TodayViewHolder.itemView.context.getString(
                        R.string.celcius_symbol
                    ) else item.tempMinFrt + this@TodayViewHolder.itemView.context.getString(R.string.fahreneit_symbol)
                this.icon.setImageResource(getSmallIcons(item.icon))
                this.windDirection.text =
                    if (hasBofor.not()) item.windspeed.toString() + item.winddirname else item.windspeedbft.toString() + item.winddirname
                this.windIndicator.rotation = item.winddir?.toFloat()!!
                this.heat.visibility = View.GONE
                this.wind.visibility = View.GONE
                this.frost.visibility = View.GONE
                this.rain.visibility = View.GONE

            }
        }
    }
}