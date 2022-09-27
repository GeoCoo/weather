package coo.apps.weather.binders.abstraction.binders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.highsoft.highcharts.common.hichartsclasses.HIChart
import com.highsoft.highcharts.common.hichartsclasses.HILine
import com.highsoft.highcharts.common.hichartsclasses.HIOptions
import com.highsoft.highcharts.common.hichartsclasses.HITitle
import coo.apps.weather.R
import coo.apps.weather.binders.abstraction.BaseItemViewBinder
import coo.apps.weather.binders.abstraction.BaseItemViewHolder
import coo.apps.weather.databinding.ChartsLayoutBinding
import coo.apps.weather.models.weather.RehlumModel
import java.util.*
import kotlin.collections.ArrayList

class RelativeHumidityChartBinder : BaseItemViewBinder<RehlumModel, RelativeHumidityChartHolder>(RehlumModel::class.java) {

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

class RelativeHumidityChartHolder(private val binding: ChartsLayoutBinding) : BaseItemViewHolder<RehlumModel>(binding.root) {

    override fun bind(data: RehlumModel) {
        binding.apply {
            val chartView = this.hc
            val options = HIOptions()
            val chart = HIChart()
            chart.type = "line"
            options.chart = chart;
            val title = HITitle()
            title.text = "Demo chart"
            options.title = title
            val series = HILine()
            series.data = data.rehlum
            options.series = ArrayList(Collections.singletonList(series))
            chartView.options = options;
        }

    }


}