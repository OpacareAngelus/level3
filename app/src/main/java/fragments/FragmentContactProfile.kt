package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.level3.databinding.FragmentContactProfileBinding
import extension.addImage

class FragmentContactProfile : Fragment() {

    private lateinit var binding: FragmentContactProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            val bundle = arguments
            addData(bundle)
        }
        binding.imgBtnBackArrow.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    private fun addData(bundle: Bundle?) {
        binding = FragmentContactProfileBinding.inflate(layoutInflater)
        binding.apply {
            ivUserPhoto.addImage(bundle?.let { FragmentContactsArgs.fromBundle(it).photo })
            tvName.text = bundle?.let { FragmentContactsArgs.fromBundle(it).name }
            tvCareer.text = bundle?.let { FragmentContactsArgs.fromBundle(it).career }
            tvUserAddress.text = bundle?.let { FragmentContactsArgs.fromBundle(it).address }
        }
    }
}

