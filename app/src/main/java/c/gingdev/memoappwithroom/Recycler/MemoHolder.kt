package c.gingdev.memoappwithroom.Recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import c.gingdev.memoappwithroom.R
import c.gingdev.memoappwithroom.db.Memo.Memo

class MemoHolder(parent: ViewGroup): RecyclerView.ViewHolder(
	LayoutInflater.from(parent.context).inflate(R.layout.layout_memo_item, parent, false)) {

	private val memoUser = itemView.findViewById<TextView>(R.id.UserID)

	private val memoTitle = itemView.findViewById<TextView>(R.id.MemoTitle)
	private val memoDate = itemView.findViewById<TextView>(R.id.MemoDate)
	private val memoContent = itemView.findViewById<TextView>(R.id.MemoContent)

	var memo: Memo? = null

	fun bindTo(memo: Memo?) {
		this.memo = memo
		memoTitle.text = memo?.MemoTitle
		memoUser.text = "${memo?.UserId} 의 메모"
		memoDate.text = String.format("%s-%s-%s"
			,memo?.MemoDate.toString().substring(0, 4)
			,memo?.MemoDate.toString().substring(4, 6)
			,memo?.MemoDate.toString().substring(6,8))
		memoContent.text = "${memo?.MemoContent}"
	}

}