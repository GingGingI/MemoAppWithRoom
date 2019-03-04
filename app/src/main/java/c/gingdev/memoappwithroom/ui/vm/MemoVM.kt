package c.gingdev.memoappwithroom.ui.vm

import androidx.lifecycle.ViewModel
import androidx.paging.Config
import androidx.paging.toLiveData
import c.gingdev.memoappwithroom.Recycler.MemoAdapter
import c.gingdev.memoappwithroom.db.Memo.Memo
import c.gingdev.memoappwithroom.db.Memo.MemoDAO
import c.gingdev.memoappwithroom.db.User.User
import io.reactivex.Completable
import java.util.*

/**
 * [LoginedActivity] 를 위한 ViewModel
 * */
class MemoVM(private val dataSource: MemoDAO, private val user: User?): ViewModel() {

	private var adapter: MemoAdapter? = null

//	Get
	var memos = dataSource.GetMemo(user!!.ID).toLiveData(Config(
	/** 한번에 불러올 페이지의 Size. */
		pageSize = 10,
	/** Null 값으로 미리 데이터를 넣어둘 것인지. */
		enablePlaceholders = true,
	/** 메모리에서 잡아두고있을 최대 데이터량. */
		maxSize = 50))

//	Set
	fun setMemo(userID: String, title: String, content: String, date: Long)
		= dataSource.Insert(Memo(UUID.randomUUID().toString(), title, content, date, userID))

//  Remove
	fun removememo(memo: Memo)
		= dataSource.Delete(memo)
	fun removeAsUser(userID: String)
		= dataSource.DelMemoAsUser(userID)

//	Update/Edit
	fun editMemo(memo: Memo)
		= dataSource.Update(memo)

//	Adapter
	fun adapter(): MemoAdapter
			= adapter ?: MemoAdapter().also { adapter = it }
	fun adapter(adapter: MemoAdapter) { this.adapter = adapter }

	companion object {
		val TAG = javaClass.simpleName
	}
}