package c.gingdev.memoappwithroom.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import c.gingdev.memoappwithroom.db.User.User
import c.gingdev.memoappwithroom.db.User.UserDAO
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * [MainActivty] 를 위한 ViewModel
 * */
class UserVM(private val dataSource: UserDAO): ViewModel() {

	fun findUser(): Flowable<String>
			= dataSource.getUserByKey(Key).map { it.Name }

	fun register(id: String, pw: String, Name: String) {
		val user = User(Key, id, pw, Name)
		Observable
			.just(user)
			.subscribeOn(Schedulers.io())
			.subscribe({
				dataSource.Insert(user)
			},{ Log.e("Error", it.message) })
	}

	fun login(id: String, pw: String): Flowable<User>
		= dataSource.Login(id, pw)

	companion object {
		private const val Key: Int = 1
	}
}