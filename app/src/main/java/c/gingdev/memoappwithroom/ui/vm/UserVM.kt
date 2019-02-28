package c.gingdev.memoappwithroom.ui.vm

import androidx.lifecycle.ViewModel
import c.gingdev.memoappwithroom.db.User.User
import c.gingdev.memoappwithroom.db.User.UserDAO
import io.reactivex.Flowable

/**
 * [MainActivty] 를 위한 ViewModel
 * */
class UserVM(private val dataSource: UserDAO): ViewModel() {

	fun findUser(): Flowable<User>
			= dataSource.getUserByKey(Key)

	fun User(id: String, pw: String): Flowable<User?>
			= dataSource.Login(id, pw)

	fun register(id: String, pw: String, Name: String)
			= dataSource.Insert(User(Key, id, pw, Name))

	companion object {
		private const val Key: Int = 1
	}
}