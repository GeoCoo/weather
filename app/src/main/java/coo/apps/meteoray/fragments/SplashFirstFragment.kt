package coo.apps.meteoray.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import coo.apps.meteoray.R
import coo.apps.meteoray.base.BaseFragment
import coo.apps.meteoray.models.NavigationDest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFirstFragment : BaseFragment() {
    override fun getLayoutRes(): Int = R.layout.fragment_splash_first

    override fun initLayout(view: View) {
        lifecycleScope.launch {
            delay(500)
            navigation.postNavigation(NavigationDest.HOME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_first, container, false)
    }


}