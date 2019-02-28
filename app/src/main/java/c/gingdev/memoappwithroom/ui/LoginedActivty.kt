package c.gingdev.memoappwithroom.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import c.gingdev.memoappwithroom.Calculate
import c.gingdev.memoappwithroom.R
import c.gingdev.memoappwithroom.Recycler.MemoAdapter
import c.gingdev.memoappwithroom.Recycler.MemoHolder
import c.gingdev.memoappwithroom.db.Memo.Memo
import c.gingdev.memoappwithroom.db.Memo.MemoDAO
import c.gingdev.memoappwithroom.db.User.User
import c.gingdev.memoappwithroom.memoInjection
import c.gingdev.memoappwithroom.ui.vm.MemoVM
import c.gingdev.memoappwithroom.ui.vm.vmFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_logined.*
import java.text.SimpleDateFormat
import java.util.*

class LoginedActivty: AppCompatActivity() {

	private val calc = Calculate
	private var value: Float = 0f
	private var editPosition: Int? = null

	private var isEdit: Boolean = false
	private var isExpanded: Boolean = false

	private lateinit var vmFactory: vmFactory<MemoDAO>

	private var user: User? = null
	private var memo: Memo? = null
	private val disposable = CompositeDisposable()

	private val memoVM by lazy(LazyThreadSafetyMode.NONE) {
		ViewModelProviders.of(this, vmFactory).get(MemoVM::class.java)
	}
	private val InsertMemoBehavior by lazy(LazyThreadSafetyMode.NONE) {
		BottomSheetBehavior.from(InsertMemoLayout)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_logined)

		intent.apply {
			user = getSerializableExtra("User") as User
			SayHello.text = (user?.Name ?: "User") + " 님 안녕하세요!"
		}

		val adapter = MemoAdapter()
		memoList.layoutManager =
			LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
		memoList.adapter = adapter

		vmFactory = memoInjection.ProvideVMFactory(this, user)
		memoVM.memos.observe(this, Observer(adapter::submitList))

		fab.setOnClickListener { addItem() }

		initSwipeGesture()
		bottomSheetInit()
	}

	private fun initSwipeGesture() {
		ItemTouchHelper(object : ItemTouchHelper.Callback() {
			override fun getMovementFlags(recyclerView: RecyclerView,
			                              viewHolder: RecyclerView.ViewHolder ): Int =
				makeMovementFlags(0, ItemTouchHelper.UP or ItemTouchHelper.DOWN)
			override fun onMove(recyclerView: RecyclerView,
			                    viewHolder: RecyclerView.ViewHolder,
			                    target: RecyclerView.ViewHolder): Boolean = false

			override fun onChildDraw(
				c: Canvas,
				recyclerView: RecyclerView,
				viewHolder: RecyclerView.ViewHolder,
				dX: Float,
				dY: Float,
				actionState: Int,
				isCurrentlyActive: Boolean) {
				super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

				val color: Int
				val icon: Int

				if (dY > 0) {
					color = Color.LTGRAY
					icon = R.drawable.ic_edit_24dp
				} else {
					color = Color.argb(255, 255, 119, 89)
					icon = R.drawable.ic_delete_24dp
				}

				if (actionState == MotionEvent.ACTION_DOWN || actionState == MotionEvent.ACTION_MOVE)
					value = calc.GetFloatValue(viewHolder.itemView.height * 0.7f, dY)

				conditionViewLayout.apply {
					background.setColorFilter(color, PorterDuff.Mode.ADD)
					scaleX = value * 2.5f
					scaleY = value * 2.5f
				}
				conditionViewImage.apply {
					setImageDrawable(ContextCompat.getDrawable(this@LoginedActivty, icon))
					scaleX = value * 1.5f
					scaleY = value * 1.5f
				}
			}

			override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
				when (direction) {
					ItemTouchHelper.UP -> {
						(viewHolder as MemoHolder).memo?.let {
							disposable.add(memoVM.removememo(it)
								.subscribeOn(Schedulers.io())
								.subscribe({
									Log.i("Success", "To Delete")
								},{ e ->
									Log.e("FailedToDelete ->", e.message)
								}))
						}
					}
					ItemTouchHelper.DOWN -> {
						(viewHolder as MemoHolder).memo?.let {
							memo = it
							isEdit = true
							editPosition = viewHolder.adapterPosition

							InsertMemoTitle.text.insert(0, it.MemoTitle)
							InsertMemoContent.text.insert(0, it.MemoContent)

							InsertMemoBehavior.state = BottomSheetBehavior.STATE_EXPANDED
						}
					}
				}

				fadeThread(300, object : fadeThread.FadeThreadListener{
					override fun onFading(value: Float) {
						this@LoginedActivty.value = value
							conditionViewLayout.let {
							it.scaleX = value * 2.5f
							it.scaleY = value * 2.5f }
						conditionViewImage.let {
							it.scaleX = value * 1.5f
							it.scaleY = value * 1.5f }
					}
				}).start()
			}
		}).attachToRecyclerView(memoList)
	}

	private fun bottomSheetInit() {
		InsertMemoBehavior.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
			override fun onSlide(bottomSheet: View, value: Float) {}
			override fun onStateChanged(bottomSheet: View, newState: Int) {
				when(newState) {
					BottomSheetBehavior.STATE_COLLAPSED,
					BottomSheetBehavior.STATE_HIDDEN -> {
						if (isEdit && memo != null)
							if (!checkIsEmpty())
								disposable.add(memoVM.editMemo(memo!!)
									.subscribeOn(Schedulers.io())
									.subscribe()).also { memo = null }

						isExpanded = false
						isEdit = false

						changeFabIcon()
					}

					BottomSheetBehavior.STATE_DRAGGING,
					BottomSheetBehavior.STATE_EXPANDED -> {
						isExpanded = true

						hideKeyBoard(InsertMemoTitle)
						hideKeyBoard(InsertMemoContent)

						changeFabIcon()
					}

					/** such like else */
					BottomSheetBehavior.STATE_HALF_EXPANDED,
					BottomSheetBehavior.STATE_SETTLING -> { /** 핫윙 */ }
				}
			}
		})
	}

	override fun onStart() {
		super.onStart()

		user = user ?: getUser()
	}
	override fun onStop() {
		super.onStop()

		disposable.clear()
	}
	private fun getUser(): User? {
//		TestUser
		return User(1,"asdf", "asdf", "Test")
	}

	fun addItem() {
		if (isExpanded) {
			/** On Editing*/
			if (isEdit && memo != null) {
				if (!checkIsEmpty()) {
					disposable.add(memoVM.editMemo(Memo(memo!!.MemoId,
						InsertMemoTitle.text.toString(), InsertMemoContent.text.toString(),
						memo!!.MemoDate, memo!!.UserId))
						.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe({
							Log.i("Update", "Success To Update")
						}, {
							Log.e("Update", "Cannot Update Data", it)
						})).also { memo = null }
				}
			}else {
				/** True == Empty
				 *  False == NotEmpty */
				if (!checkIsEmpty())
					disposable.add(memoVM.setMemo(user?.ID ?: "User",
						InsertMemoTitle.text.toString(), InsertMemoContent.text.toString(),
						getDateAsLong())
						.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe({
							Log.i("SuccessToInsert", user?.ID.toString())
						}, {
							Log.e("FailedToInsert", it.message)
						}))
			}
			InsertMemoBehavior.state = BottomSheetBehavior.STATE_HIDDEN
		} else
			InsertMemoBehavior.state = BottomSheetBehavior.STATE_EXPANDED

		InsertMemoTitle.let {
			it.text.clear()
			hideKeyBoard(it)
		}
		InsertMemoContent.let {
			it.text.clear()
			hideKeyBoard(it)
		}
	}

	private fun changeFabIcon() {
		when {
			isEdit -> fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_edit_24dp))
			isExpanded -> fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_done_24dp))
			else -> fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add_24dp))
		}
	}
	private fun hideKeyBoard(v: View) {
		val imm = applicationContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
		v.clearFocus()
		imm.hideSoftInputFromWindow(v.windowToken, 0)
	}
	@SuppressLint("SimpleDateFormat") private fun getDateAsLong(): Long {
		val calendar = Calendar.getInstance()
		val format = SimpleDateFormat("yyyyMMddHHmmss")
		return format.format(calendar.time).toLong()
	}
	private fun checkIsEmpty(): Boolean {
		return (InsertMemoTitle.text.isEmpty() or InsertMemoContent.text.isEmpty())
	}

	private class fadeThread: Thread {
		constructor(duration: Long, callback: FadeThreadListener) {
			this.value = 1.0f
			this.duration = duration
			this.callback = callback
		}

		var value: Float = 1.0f
		val duration: Long
		val callback: FadeThreadListener

		override fun run() {
			super.run()
			while (value > 0f) {
				Thread.sleep(duration / 100)
				callback.onFading(value).also { value -= 0.01f }
			}
		}

		interface FadeThreadListener {
			fun onFading(value: Float)
		}
	}
}