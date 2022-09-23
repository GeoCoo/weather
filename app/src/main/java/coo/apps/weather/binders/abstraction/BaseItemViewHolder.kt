package coo.apps.weather.binders.abstraction

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.RecyclerView

abstract class BaseItemViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView), LifecycleObserver {

    abstract fun bind(data: T)

    fun getContext(): Context = itemView.context

    // Sometimes bind will not be called, if a viewHolder is cached.
    // You should rebind your RxJava subscribers here, for when this happens
    open fun onAttach() {}

    open fun onDetach() {}

    open fun bind(data: T, payloads: List<Any>) {
        bind(data)
    }
}