package coo.apps.weather.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coo.apps.weather.R
import coo.apps.weather.base.BaseFragment
import coo.apps.weather.databinding.FragmentHomeBinding
import kotlinx.coroutines.runBlocking

class HomeFragment : BaseFragment() {

    private var binding: FragmentHomeBinding? = null

    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun initLayout(view: View) {
        runBlocking {
            mainViewModel.makeMainRequest()
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}