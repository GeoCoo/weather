package coo.apps.weather.binders.abstraction

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.WeakReference

abstract class BaseItemViewBinder<M, in VH : BaseItemViewHolder<M>>(val modelClass: Class<out M>) : DiffUtil.ItemCallback<M>() {

    private var weakLifecycle: WeakReference<Lifecycle>? = null

    abstract fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun bindViewHolder(model: M, viewHolder: VH)
    abstract fun getItemType(): Int

    fun postCreateViewHolder(viewHolder: VH){
        weakLifecycle?.get()?.addObserver(viewHolder)
    }

    fun registerLifecycle(lifecycle: Lifecycle){
        weakLifecycle = WeakReference(lifecycle)
    }

    open fun onViewAttachedToWindow(viewHolder: VH) {
        viewHolder.onAttach()
    }

    open fun onViewRecycled(viewHolder: VH) {
        viewHolder.onDetach()
    }

    open fun onViewDetachedFromWindow(viewHolder: VH) {
        viewHolder.onDetach()
    }
}