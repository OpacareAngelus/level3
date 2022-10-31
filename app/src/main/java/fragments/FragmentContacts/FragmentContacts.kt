package fragments.FragmentContacts

import adapter.RecyclerAdapterUserContacts
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.level3.R
import com.example.level3.databinding.FragmentContactsBinding
import com.google.android.material.snackbar.Snackbar
import data.model.User
import fragments.FragmentAddContact
import util.UserListController

class FragmentContacts : Fragment(), UserListController {

    private lateinit var binding: FragmentContactsBinding

    private val viewModel: FragmentContactsViewModel by viewModels()

    private val usersAdapter by lazy {
        RecyclerAdapterUserContacts(userListController = this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactsBinding.inflate(layoutInflater)

        binding.tvAddContact.setOnClickListener {
            FragmentAddContact(userListController = this).apply {
                show(
                    this@FragmentContacts.requireActivity().supportFragmentManager,
                    "AddContact",
                )
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.rvContacts.apply { adapter = usersAdapter }
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView)

        setObservers()
    }

    override fun onContactAdd(user: User) {
        viewModel.add(user)
    }

    override fun onDeleteUser(user: User) {
        viewModel.deleteUser(user)
    }

    override fun onOpenContactProfile(user: User) {
        findNavController().navigate(
            R.id.action_fragmentContacts_to_fragmentContactProfile,
            bundleOf(
                Pair(getString(R.string.argument_contacts_to_contact_profile), user)
            )
        )
    }

    private fun setObservers() {
        viewModel.userListLiveData.observe(viewLifecycleOwner) {
            usersAdapter.submitList(it)
        }
    }

    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val user = viewModel.getUser(viewHolder.absoluteAdapterPosition) as User
            when (direction) {
                ItemTouchHelper.LEFT -> {
                    val delMessage = Snackbar.make(
                        viewHolder.itemView,
                        "${user.name} has deleted.",
                        Snackbar.LENGTH_LONG
                    )
                    onDeleteUser(user)
                    delMessage.setAction(R.string.cancel) {
                        onContactAdd(user)
                    }
                    delMessage.show()
                }
                ItemTouchHelper.RIGHT -> {
                    onOpenContactProfile(user)
                }
            }
        }
    }
}