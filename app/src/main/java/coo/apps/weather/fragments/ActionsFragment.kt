package coo.apps.weather.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import coo.apps.weather.databinding.FragmentActionsBinding


class ActionsFragment : BottomSheetDialogFragment() {

    companion object {
            val TAG = "ActionsFragment"
    }

    private lateinit var binding: FragmentActionsBinding

    //creating the Dialog Fragment.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentActionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    //tasks that need to be done after the creation of Dialog
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {


        binding.save.setOnClickListener {
            dismiss()
        }
        binding.view.setOnClickListener {
            dismiss()
        }
    }
}

