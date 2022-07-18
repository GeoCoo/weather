package coo.apps.weather.base

import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import coo.apps.weather.viemodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class BaseFragment : Fragment(){

    private var fragmentInitialized = false
    var isInForeground = false

    protected val mainViewModel: MainViewModel by sharedViewModel()
    abstract fun getLayoutRes(): Int
    abstract fun initLayout(view: View)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutRes(), container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentInitialized = true
        isInForeground = true
        initLayout(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentInitialized = false
    }

    override fun onPause() {
        super.onPause()
        isInForeground = false
    }



}