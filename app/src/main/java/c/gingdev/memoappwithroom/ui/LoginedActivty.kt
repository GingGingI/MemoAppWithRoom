package c.gingdev.memoappwithroom.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import c.gingdev.memoappwithroom.R
import c.gingdev.memoappwithroom.Recycler.MemoAdapter
import c.gingdev.memoappwithroom.db.Memo.MemoDAO
import c.gingdev.memoappwithroom.db.User.User
import c.gingdev.memoappwithroom.memoInjection
import c.gingdev.memoappwithroom.ui.vm.MemoVM
import c.gingdev.memoappwithroom.ui.vm.vmFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_logined.*

class LoginedActivty: AppCompatActivity() {

	private lateinit var vmFactory: vmFactory<MemoDAO>
	private val memoVM by lazy(LazyThreadSafetyMode.NONE) {
		ViewModelProviders.of(this, vmFactory).get(MemoVM::class.java)
	}

	private var user: User? = null

	private val disposable = CompositeDisposable()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_logined)

		intent.apply {
			user = getSerializableExtra("User") as User
			SayHello.text = user?.Name ?: "User" + "님 안녕하세요!"
		}

		val adapter = MemoAdapter()
		memoList.adapter = adapter

		vmFactory = memoInjection.ProvideVMFactory(this, user)
		memoVM.memos.observe(this, Observer(adapter::submitList))

		fab.setOnClickListener { addItem() }
	}

	override fun onStart() {
		super.onStart()

		user = user ?: getUser()
	}

	private fun getUser(): User? {
//		TestUser
		return User(1,"asdf", "asdf", "Test")
	}

	override fun onStop() {
		super.onStop()

		disposable.clear()
	}

//	fun getDate(): Long
//		= Date.

	fun addItem() {
		Toast.makeText(this, "itemAdd", Toast.LENGTH_LONG).show()
		disposable.add(memoVM.setMemo(user?.ID ?: "User", "testTitle", 20190226 ,"TestContent")
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe({
				Toast.makeText(this, "SuccessToInsert", Toast.LENGTH_LONG).show()
				}, {
				Log.e("FailedToInsert", it.message)
			}))
	}
}