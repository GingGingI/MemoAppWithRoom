package c.gingdev.memoappwithroom.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import c.gingdev.memoappwithroom.db.User.UserDAO
import java.lang.IllegalArgumentException

class vmFactory(private val dataSource: UserDAO): ViewModelProvider.Factory {
	override fun <T: ViewModel?> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(UserVM::class.java)) {
			return UserVM(dataSource) as T
		}
		throw IllegalArgumentException("Unknown ViewModel class")
	}
}