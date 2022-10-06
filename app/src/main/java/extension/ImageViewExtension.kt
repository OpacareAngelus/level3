package extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import model.User

fun ImageView.addImage(user: User) {
    Glide.with(this.context)
        .load(user.photo)
        .into(this)
}