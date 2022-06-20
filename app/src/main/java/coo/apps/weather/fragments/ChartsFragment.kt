package coo.apps.weather.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coo.apps.weather.databinding.FragmentChartsBinding
import coo.apps.weather.viemodels.NotificationsViewModel

class NotificationsFragment : Fragment() {

    private var binding: FragmentChartsBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val notificationsViewModel = ViewModelProvider(this)[NotificationsViewModel::class.java]

        binding = FragmentChartsBinding.inflate(inflater, container, false)

        notificationsViewModel.text.observe(viewLifecycleOwner) {
            binding?.textNotifications?.text = it
        }
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}