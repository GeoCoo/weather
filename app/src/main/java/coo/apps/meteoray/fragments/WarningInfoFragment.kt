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
import coo.apps.meteoray.databinding.FragmentWarningInfoBinding


class WarningInfoFragment : BaseFragment() {
    private var _binding: FragmentWarningInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var backArrow: Drawable


    override fun getLayoutRes(): Int = R.layout.fragment_warning_info
    override fun initLayout(view: View) {
        setUpToolbar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWarningInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("RestrictedApi")
    private fun setUpToolbar() {
        actionBar.apply {
            this.show()
            this.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorWhite)))
            this.title = resources.getString(R.string.warning_info)
            this.setHomeButtonEnabled(true)
            this.setDisplayHomeAsUpEnabled(true)
            this.setShowHideAnimationEnabled(false)
            backArrow =
                resources.getDrawable(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            backArrow.setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_ATOP)
            this.setHomeAsUpIndicator(backArrow)

        }
    }

}

