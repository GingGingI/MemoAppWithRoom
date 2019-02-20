package c.gingdev.memoappwithroom.ui.vm

import androidx.lifecycle.ViewModel
import c.gingdev.memoappwithroom.db.Memo.MemoDAO

class MemoVM(private val dataSource: MemoDAO): ViewModel() {

	companion object {
		val TAG = javaClass.simpleName
	}
}