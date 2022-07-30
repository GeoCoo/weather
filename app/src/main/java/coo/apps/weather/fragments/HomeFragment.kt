package coo.apps.weather.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coo.apps.weather.R
import coo.apps.weather.base.BaseFragment
import coo.apps.weather.databinding.FragmentHomeBinding
import coo.apps.weather.models.main.MainResponse
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.lifecycle.Observer

class HomeFragment : BaseFragment() {

    private var binding: FragmentHomeBinding? = null
    private var response: MainResponse? = null

    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun initLayout(view: View) {
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalScope.launch(IO) {
            response = mainViewModel.makeMainRequest()
            response
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}