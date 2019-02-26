package c.gingdev.memoappwithroom

import android.content.Context
import c.gingdev.memoappwithroom.db.Memo.MemoDAO
import c.gingdev.memoappwithroom.db.Memo.MemoDataBase
import c.gingdev.memoappwithroom.db.User.User
import c.gingdev.memoappwithroom.ui.vm.vmFactory

object memoInjection {

	fun ProvideMemoDataSource(context: Context): MemoDAO {
		val dataBase = MemoDataBase.getInstance(context)
		return dataBase.getMemoDAO()
	}

	fun ProvideVMFactory(context: Context, user: User?): vmFactory<MemoDAO> {
		val dataSource = ProvideMemoDataSource(context)
		return vmFactory(dataSource, user)
	}
}