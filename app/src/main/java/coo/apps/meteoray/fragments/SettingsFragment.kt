package coo.apps.meteoray.fragments

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import coo.apps.meteoray.BuildConfig
import coo.apps.meteoray.R
import coo.apps.meteoray.base.BaseFragment
import coo.apps.meteoray.databinding.FragmentSettingsBinding
import coo.apps.meteoray.models.NavigationDest


class SettingsFragment : BaseFragment(), OnClickListener {
    private lateinit var backArrow: Drawable
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun getLayoutRes(): Int = R.layout.fragment_settings

    override fun initLayout(view: View) {
        setUpToolbar()
        setListeners()
        handleFahreneitChoice()
        handleWindChoice()
        setInfo()
    }

    private fun setInfo() {
        binding.versionInfo.text =
            resources.getString(R.string.version_setting, BuildConfig.VERSION_CODE.toString())
        binding.releaseInfo.text =
            resources.getString(R.string.release_date_setting, BuildConfig.RELEASE_DATE)
    }


    private fun handleWindChoice() {
        binding.bofoSwitch.isChecked = getSharedPref("bofor")
        binding.bofoSwitch.setOnCheckedChangeListener { _, isChecked ->
            setBofortChoice(isChecked)
        }
    }

    private fun handleFahreneitChoice() {
        binding.fahreneitSwitch.isChecked = getSharedPref("fahreneit")
        binding.fahreneitSwitch.setOnCheckedChangeListener { _, isChecked ->
            setFahreneitChoice(isChecked)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("RestrictedApi")
    private fun setUpToolbar() {
        actionBar.apply {
            this.show()
            this.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorWhite)))
            this.title = resources.getString(R.string.title_settings)
            this.setHomeButtonEnabled(true)
            this.setDisplayHomeAsUpEnabled(true)
            this.setShowHideAnimationEnabled(false)
            backArrow = resources.getDrawable(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            backArrow.setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_ATOP)
            this.setHomeAsUpIndicator(backArrow)

        }
    }

    private fun setListeners() {
        binding.locationsCl.setOnClickListener(this)
        binding.descriptionCl.setOnClickListener(this)
        binding.projectFundingCl.setOnClickListener(this)
        binding.disclaimerCL.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.locationsCl -> {
                navigation.postNavigation(Pair(NavigationDest.LOCATIONS, null))
            }

            binding.descriptionCl -> {
                navigation.postNavigation(Pair(NavigationDest.INFO, null))
                navigation.postDestinationNav(R.string.description_setting)
            }

            binding.projectFundingCl -> {
                navigation.postNavigation(Pair(NavigationDest.INFO, null))
                navigation.postDestinationNav(R.string.project_funding_setting)
            }

            binding.disclaimerCL -> {
                navigation.postNavigation(Pair(NavigationDest.INFO, null))
                navigation.postDestinationNav(R.string.disclaimer_setting)

            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}