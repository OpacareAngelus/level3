package ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.level3.databinding.AddContactBinding

class AddContactActivity : AppCompatActivity() {

    private lateinit var binding: AddContactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddContactBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setListeners()
    }
    private fun setListeners() {
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.btnSaveContact.setOnClickListener{
        }
    }
}