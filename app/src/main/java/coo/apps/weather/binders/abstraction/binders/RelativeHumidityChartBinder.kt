package coo.apps.weather.binders.abstraction.binders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coo.apps.weather.R
import coo.apps.weather.binders.abstraction.BaseItemViewBinder
import coo.apps.weather.binders.abstraction.BaseItemViewHolder
import coo.apps.weather.databinding.ChartsLayoutBinding
import coo.apps.weather.models.weather.RehlumModel

class RelativeHumidityChartBinder() : BaseItemViewBinder<RehlumModel, RelativeHumidityChartHolder>(RehlumModel::class.java) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        RelativeHumidityChartHolder(ChartsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun bindViewHolder(model: RehlumModel, viewHolder: RelativeHumidityChartHolder) {
        viewHolder.bind(model)
    }

    override fun getItemType(): Int = R.layout.charts_layout
    override fun areItemsTheSame(oldItem: RehlumModel, newItem: RehlumModel) = oldItem.javaClass == newItem.javaClass

    override fun areContentsTheSame(oldItem: RehlumModel, newItem: RehlumModel): Boolean {
        return oldItem == newItem
    }

}

class RelativeHumidityChartHolder(val binding: ChartsLayoutBinding) : BaseItemViewHolder<RehlumModel>(binding.root) {

    override fun bind(data: RehlumModel) {

    }


}