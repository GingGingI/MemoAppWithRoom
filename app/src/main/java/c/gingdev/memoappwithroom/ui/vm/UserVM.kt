package c.gingdev.memoappwithroom.ui.vm

import androidx.lifecycle.ViewModel
import c.gingdev.memoappwithroom.db.User.User
import c.gingdev.memoappwithroom.db.User.UserDAO
import io.reactivex.Flowable
import java.util.*

/**
 * [MainActivty] 를 위한 ViewModel
 * */
class UserVM(private val dataSource: UserDAO): ViewModel() {

	fun findUser(Key: String): Flowable<User>
			= dataSource.getUserByKey(Key)

	fun User(id: String, pw: String): Flowable<User?>
			= dataSource.Login(id, pw)

	fun register(Key: String, id: String, pw: String, Name: String)
			= dataSource.Insert(User(Key, id, pw, Name))

	companion object {
		val TAG = javaClass.simpleName
	}
}