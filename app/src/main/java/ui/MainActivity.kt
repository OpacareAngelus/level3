package ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.level3.R
import model.UsersViewModel

class MainActivity : AppCompatActivity() {

    val userList : UsersViewModel = UsersViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

