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
        val bundle = arguments
        addData(bundle)
        binding.imgBtnBackArrow.setOnClickListener{
            findNavController().navigate(R.id.action_fragmentContactProfile_to_fragmentContacts)
        }
        return binding.root
    }

    private fun addData(bundle: Bundle?) {
        binding = ContactProfileBinding.inflate(layoutInflater)
        binding.ivUserPhoto.addImage(bundle?.let { FragmentContactsArgs.fromBundle(it).photo })
        binding.tvName.text = bundle?.let { FragmentContactsArgs.fromBundle(it).name }
        binding.tvCareer.text = bundle?.let { FragmentContactsArgs.fromBundle(it).career }
        binding.tvUserAddress.text = bundle?.let { FragmentContactsArgs.fromBundle(it).address }
    }
}

private fun ImageView.addImage(photo: String?) {
    Glide.with(this.context)
        .load(photo)
        .into(this)
}
