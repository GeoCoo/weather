package coo.apps.weather.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coo.apps.weather.R
import coo.apps.weather.base.BaseFragment
import coo.apps.weather.databinding.FragmentActionsBinding


class ActionsFragment : BaseFragment() {
    private lateinit var binding: FragmentActionsBinding


    override fun getLayoutRes() = R.layout.fragment_actions

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentActionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initLayout(view: View) {
    }

}

