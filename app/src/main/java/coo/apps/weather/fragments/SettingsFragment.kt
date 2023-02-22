package coo.apps.weather.fragments

import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import coo.apps.weather.R
import coo.apps.weather.base.BaseFragment
import coo.apps.weather.databinding.FragmentSettingsBinding
import coo.apps.weather.models.NavigationDest


class SettingsFragment : BaseFragment(), OnClickListener {
    private lateinit var backArrow: Drawable
    private lateinit var binding: FragmentSettingsBinding

    override fun getLayoutRes(): Int = R.layout.fragment_settings

    override fun initLayout(view: View) {
        setUpToolbar()
        setListeners()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setUpToolbar() {
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar.apply {
            this?.show()
            this?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorWhite)))

            this?.title = "Settings"
            this?.setHomeButtonEnabled(true)
            this?.setDisplayHomeAsUpEnabled(true)
            backArrow =
                resources.getDrawable(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            backArrow.setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_ATOP)
            this?.setHomeAsUpIndicator(backArrow)
        }
    }

    private fun setListeners() {
        binding.locationsCl.setOnClickListener(this)
        binding.privacyPolicyCl.setOnClickListener(this)
        binding.disclaimerCL.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.locationsCl -> {
                navigation.handleNavigation(navView, NavigationDest.LOCATIONS)

            }
        }

    }


}