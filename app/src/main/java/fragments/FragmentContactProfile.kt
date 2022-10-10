package fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.level3.R
import com.example.level3.databinding.ContactProfileBinding

class FragmentContactProfile : Fragment() {

    private lateinit var binding: ContactProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addData()
        binding.imgBtnBackArrow.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentContactProfile_to_fragmentContacts)
        }
        return binding.root
    }

    private fun addData() {
        binding = ContactProfileBinding.inflate(layoutInflater)
        binding.ivUserPhoto.addImage(requireArguments().getString(photo))
        binding.tvName.text = requireArguments().getString(name)
        binding.tvCareer.text = requireArguments().getString(career)
        binding.tvUserAddress.text = requireArguments().getString(address)
    }

    companion object {
        const val photo = "photo"
        const val name = "name"
        const val career = "career"
        const val address = "address"
    }

}

private fun ImageView.addImage(photo: String?) {
    Glide.with(this.context)
        .load(photo)
        .into(this)
}
