package c.gingdev.memoappwithroom.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import c.gingdev.memoappwithroom.R
import c.gingdev.memoappwithroom.db.User.User
import c.gingdev.memoappwithroom.db.User.UserDAO
import c.gingdev.memoappwithroom.loginInjection
import c.gingdev.memoappwithroom.ui.vm.UserVM
import c.gingdev.memoappwithroom.ui.vm.vmFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

	private lateinit var vmFactory: vmFactory<UserDAO>
	private lateinit var userVM: UserVM

	private val disposable = CompositeDisposable()

	private var registerMode: Boolean? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		vmFactory = loginInjection.ProvideVMFactory(this)
		userVM = ViewModelProviders.of(this, vmFactory).get(UserVM::class.java)

		Login.setOnClickListener { login() }
		Register.setOnClickListener { Register() }
	}

	override fun onStart() {
		super.onStart()
		disposable.add(userVM.findUser()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe({ login(it.ID, it.Password) },
				{ error -> Log.e("MainActivity", "Unable to get name", error)}))
	}

	override fun onStop() {
		super.onStop()

		disposable.clear()
	}

	private fun login(userID: String, userPW: String) {
		Login.isEnabled = false

		if (registerMode == null) {
			disposable.add(userVM.User(userID, userPW)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.doOnSubscribe { this.Login.isEnabled = true; clearEditTextView() }
				.doOnTerminate { this.Login.isEnabled = true }
				.subscribe({
					if (it != null) {
						startActivity(Intent(this, LoginedActivty::class.java).apply { putExtra("User", it) })
						finish()
					} else {
						Log.e("Login", "WrongUser")
					}
				}, {
					Log.e("An Error Found ->", it.message)
				}))
		} else {
			val userName = UserName.text.toString()

			disposable.add(userVM.register(userID, userPW, userName)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe({
					this.Login.isEnabled = true
					unRegister()
				}, {
					Log.e("Error Found ->", it.message)
				}))
		}
	}

	private fun login() {
		val userID = UserID.text.toString()
		val userPW = UserPW.text.toString()

		login(userID, userPW)
	}

	private fun clearEditTextView() {
		this.UserID.text.clear()
		this.UserPW.text.clear()
		this.UserName.text.clear()
	}

	private fun Register() {
		registerMode = true

		UserName.visibility = View.VISIBLE
		Register.visibility = View.GONE

		Login.text = this.getText(R.string.Register)
	}
	private fun unRegister() {
		registerMode = null

		UserName.visibility = View.GONE
		Register.visibility = View.VISIBLE

		Login.text = this.getText(R.string.Login)
	}

	override fun onBackPressed() {
		super.onBackPressed()
		if (registerMode != null) unRegister()
	}
}
