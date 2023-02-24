package coo.apps.meteoray.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coo.apps.meteoray.R
import coo.apps.meteoray.base.BaseFragment
import coo.apps.meteoray.databinding.FragmentInfoBinding

class InfoFragment : BaseFragment() {
    private lateinit var binding: FragmentInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutRes(): Int = R.layout.fragment_info

    override fun initLayout(view: View) {
        TODO("Not yet implemented")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

}