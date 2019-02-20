package c.gingdev.memoappwithroom.db.User

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(User::class), version = 1)
abstract class UserDataBase: RoomDatabase() {

    abstract fun getUserDAO(): UserDAO

    companion object {
        private var USER_INSTANCE: UserDataBase? = null

        fun getInstance(context: Context): UserDataBase {
            if (USER_INSTANCE == null) {
                synchronized(UserDataBase::class.java) {
                    USER_INSTANCE = Room.databaseBuilder(
                        context,
                        UserDataBase::class.java,
                        "userdb.db").build()
                }
            }
            return USER_INSTANCE!!
        }
    }

}