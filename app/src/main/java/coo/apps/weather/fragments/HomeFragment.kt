package coo.apps.weather.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import coo.apps.weather.R
import coo.apps.weather.base.BaseFragment
import coo.apps.weather.databinding.FragmentHomeBinding
import coo.apps.weather.models.main.MainResponse
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : BaseFragment() {

    private var binding: FragmentHomeBinding? = null
    private lateinit var response: MainResponse

    override fun getLayoutRes(): Int = R.layout.fragment_home

    override fun initLayout(view: View) {
        lifecycleScope.launch {
            response = mainViewModel.makeMainRequest()!!
            setUpCurrent(response)
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }


    private fun setUpCurrent(response: MainResponse?) {
        binding?.apply {
//            val uri: Uri = Uri.parse("http://api.wassf.net/" + response?.current?.icon)
//
//            GlideToVectorYou
//                .init()
//                .with(activity)
//
//                .load(uri, wearherSymbol);
            placeTxt.text = mainViewModel.getPlaceName()
            temperature.text = response?.current?.temp
            weatherType.text = response?.current?.desc
            humidity.text = resources.getString(R.string.humidity_tag, response?.current?.relhum)
            rain.text = resources.getString(R.string.rain_tag, response?.current?.precip)
            wind.text = resources.getString(R.string.wind_tag, response?.current?.wind10, response?.current?.wind10dir)
            dust.text = resources.getString(R.string.dust_tag, response?.current?.dust)
            visibility.text = resources.getString(R.string.visibility_tag, response?.current?.vis)


        }


    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}