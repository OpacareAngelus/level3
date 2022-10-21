package fragments

import SimpleCallBack
import adapter.RecyclerAdapterUserContacts
import adapter.UserListController
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.level3.databinding.AddContactBinding
import com.example.level3.databinding.FragmentContactsBinding
import model.User
import model.UsersViewModel

class FragmentContacts : Fragment(), UserListController {

    private lateinit var binding: FragmentContactsBinding
    private lateinit var dialogBinding: AddContactBinding

    private val viewModel: UsersViewModel by viewModels()

    private val usersAdapter by lazy {
        RecyclerAdapterUserContacts(userListController = this, findNavController())
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
        binding = FragmentContactsBinding.inflate(layoutInflater)

        binding.tvAddContact.setOnClickListener {
            dialogCreate(inflater)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()

        val recyclerView: RecyclerView = binding.rvContacts.apply { adapter = usersAdapter }
        recyclerView.layoutManager = LinearLayoutManager(activity)

        ItemTouchHelper(
            SimpleCallBack(
                viewModel,
                usersAdapter,
                findNavController()
            ).simpleCallback
        ).attachToRecyclerView(recyclerView)

    }

    override fun onContactAdd(user: User) {
        usersAdapter.submitList(viewModel.userListLiveData.value.also {
            viewModel.userListLiveData.value?.add(user)
        }?.toMutableList())
    }

    override fun onDeleteUser(user: User) {
        usersAdapter.submitList(viewModel.userListLiveData.value.also {
            viewModel.userListLiveData.value?.remove(
                user
            )
        }?.toMutableList())
    }

    private fun setObservers() {
        viewModel.userListLiveData.observe(viewLifecycleOwner) {
            usersAdapter.submitList(viewModel.userListLiveData.value?.toMutableList())
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
        usersAdapter.submitList(viewModel.userListLiveData.value.also {
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
        }?.toMutableList())
        dialog.dismiss()
    }
}