package c.gingdev.memoappwithroom

import android.content.Context
import c.gingdev.memoappwithroom.db.User.UserDAO
import c.gingdev.memoappwithroom.db.User.UserDataBase
import c.gingdev.memoappwithroom.ui.vm.vmFactory

object loginInjection {

//	데이터 소스 준비작업.
	fun ProvideUserDataSource(content: Context): UserDAO {
		val database = UserDataBase.getInstance(content)
		return database.getUserDAO()
	}

//
	fun ProvideVMFactory(context: Context): vmFactory<UserDAO> {
		val dataSource = ProvideUserDataSource(context)
		return vmFactory(dataSource)
	}
}