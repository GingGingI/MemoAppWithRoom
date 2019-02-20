package c.gingdev.memoappwithroom

import android.content.Context
import c.gingdev.memoappwithroom.db.Memo.MemoDAO
import c.gingdev.memoappwithroom.db.Memo.MemoDataBase
import c.gingdev.memoappwithroom.ui.vm.vmFactory

object memoInjection {

	fun ProvideMemoDataSource(context: Context): MemoDAO {
		val dataSource = MemoDataBase.getInstance(context)
		return dataSource.getMemoDAO()
	}

	fun ProvideVMFactory(context: Context): vmFactory<MemoDAO> {
		val dataSource = ProvideMemoDataSource(context)
		return vmFactory(dataSource)
	}
}