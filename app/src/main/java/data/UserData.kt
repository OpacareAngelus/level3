package data

import model.User

object UserData {

    fun getUsers() = arrayListOf(
    User(
    0,
    "https://www.meme-arsenal.com/memes/2077f9271f077a9065e718d4ad7d1636.jpg",
    "Бурий",
    "Важлива шишка-кішка",
    "",
    "",
    "м.Київ",
    ""
    ),
    User(
    0,
    "https://st.depositphotos.com/1072614/4661/i/600/depositphotos_46614201-stock-photo-housewife-cat.jpg",
    "Ельвіра",
    "Домогосподарка",
    "",
    "",
    "м.Львів",
    ""
    ),
    User(
    0,
    "https://images.prom.ua/3469991393_w640_h640_almaznaya-mozaika-kot.jpg",
    "Мірінда",
    "Доктор котячих наук",
    "",
    "",
    "Дніпро",
    ""
    ),
    User(
    0,
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSWOUS_MuhMpwAp4L-7E9Dh091zsMCGxWmrBw&usqp=CAU",
    "Андрій",
    "Бойовий кіт",
    "",
    "",
    "Харків",
    ""
    )
    )

    fun getSize(): Int {
        return getUsers().size
    }
}