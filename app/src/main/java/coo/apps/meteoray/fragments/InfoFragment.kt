package coo.apps.meteoray.fragments

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coo.apps.meteoray.R
import coo.apps.meteoray.base.BaseFragment
import coo.apps.meteoray.databinding.FragmentInfoBinding

class InfoFragment : BaseFragment() {
    private lateinit var backArrow: Drawable
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!


    override fun getLayoutRes(): Int = R.layout.fragment_info

    override fun initLayout(view: View) {
        setUpToolbar()
    }


    @SuppressLint("RestrictedApi")
    private fun setUpToolbar() {
        actionBar.apply {
            this.show()
            this.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorWhite)))
            navigation.observerDestinationNav(viewLifecycleOwner) { title ->
                this.title = resources.getString(title)
                setUpInfo(title)
            }
            this.setHomeButtonEnabled(true)
            this.setDisplayHomeAsUpEnabled(true)
            this.setShowHideAnimationEnabled(false)
            backArrow =
                resources.getDrawable(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            backArrow.setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_ATOP)
            this.setHomeAsUpIndicator(backArrow)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpInfo(title: Int) {
        when (title) {
            R.string.disclaimer_setting -> {
                binding.info.text = resources.getString(R.string.disclaimer_info)
            }

            R.string.project_funding_setting -> {
                binding.info.text = resources.getString(R.string.project_funding_info)
            }

            R.string.description_setting -> {
                binding.info.text = resources.getString(R.string.description_info)
            }
        }

    }
}