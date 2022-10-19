package fragments

import MyItemTouchHelper
import adapter.RecyclerAdapter
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.level3.databinding.AddContactBinding
import com.example.level3.databinding.MyContactsBinding
import model.User
import model.UsersViewModel

class FragmentContacts : Fragment() {

    private lateinit var binding: MyContactsBinding
    private lateinit var dialogBinding: AddContactBinding

    private val viewModel: UsersViewModel by viewModels()

    private val usersAdapter by lazy {
        RecyclerAdapter(
            viewModel,
            onDeleteUser = { user ->
                viewModel.userListLiveData.value?.remove(user)
            }
        )
    }

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var contactPhoto: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    contactPhoto = data?.data!!
                    dialogBinding.ivUserPhoto.setImageURI(data.data!!)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MyContactsBinding.inflate(layoutInflater)

        binding.tvAddContact.setOnClickListener {
            dialogCreate(inflater)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()

        val recyclerView: RecyclerView = binding.rvContacts
        recyclerView.adapter = usersAdapter
        ItemTouchHelper(MyItemTouchHelper(viewModel, usersAdapter, findNavController()).simpleCallback).attachToRecyclerView(recyclerView)
    }

    private fun setObservers() {
        viewModel.userListLiveData.observe(viewLifecycleOwner) {
            usersAdapter.submitList(viewModel.userListLiveData.value)
        }
    }

    //add user dialog here

    private fun dialogCreate(inflater: LayoutInflater) {
        val dialog = Dialog(inflater.context)
        dialogBinding = AddContactBinding.inflate(inflater)
        dialog.setContentView(dialogBinding.root)
        setDialogListeners(dialog)
        dialog.show()
    }

    private fun setDialogListeners(dialog: Dialog) {
        dialogBinding.imgBtnBackArrow.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btnSaveContact.setOnClickListener {
            saveButtonAction(dialog)
        }
        dialogBinding.ivAddPhoto.setOnClickListener() {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intent)
        }
    }

//    I just was test validation here.
//    Right now no reason to use that it's just test are field empty or not.
//    private fun saveButtonListener(dialog: Dialog) {
//        if (dialogBinding.tietUsername.text.toString() != "" &&
//            dialogBinding.tietCareer.text.toString() != "" &&
//            dialogBinding.tietEmail.text.toString() != "" &&
//            dialogBinding.tietPhone.text.toString() != "" &&
//            dialogBinding.tietAddress.text.toString() != "" &&
//            dialogBinding.tietDataOfBirth.text.toString() != ""
//        ) {
//            saveButtonAction(dialog)
//        } else {
//            Toast.makeText(
//                dialog.context,
//                getString(R.string.error_wrong_user_data),
//                Toast.LENGTH_LONG
//            ).show()
//        }
//    }

    private fun saveButtonAction(dialog: Dialog) {
        viewModel.userListLiveData.value?.add(
            User(
                0,
                contactPhoto.toString(),
                dialogBinding.tietUsername.text.toString(),
                dialogBinding.tietCareer.text.toString(),
                dialogBinding.tietEmail.text.toString(),
                dialogBinding.tietPhone.text.toString(),
                dialogBinding.tietAddress.text.toString(),
                dialogBinding.tietDataOfBirth.text.toString()
            )
        )
        usersAdapter.itemCount.let { usersAdapter.notifyItemRangeChanged(0, it) }
        dialog.dismiss()
    }
}