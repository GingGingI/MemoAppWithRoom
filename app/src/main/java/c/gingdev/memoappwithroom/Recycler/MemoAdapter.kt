package c.gingdev.memoappwithroom.Recycler

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import c.gingdev.memoappwithroom.db.Memo.Memo

class MemoAdapter: PagedListAdapter<Memo, MemoHolder>(diffCallback) {

	override fun onBindViewHolder(holder: MemoHolder, position: Int) {
		holder.bindTo(getItem(position))
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoHolder
		= MemoHolder(parent)

	companion object {
		private val diffCallback = object: DiffUtil.ItemCallback<Memo>() {
			/** data list에 변경사항이 있을경우 예(: itemAdd)
			 * PageListAdapter가 diffCallback을 사용하여
			 * 이전데이터와 어느것이 다른지 확인 후.
			 * 데이터 rebind 및 Animate*/
			override fun areItemsTheSame(oldItem: Memo, newItem: Memo): Boolean
				= oldItem.MemoId == newItem.MemoId
			/** 데이터가 동일한지 검사.*/
			override fun areContentsTheSame(oldItem: Memo, newItem: Memo): Boolean
				= oldItem == newItem
		}
	}
}