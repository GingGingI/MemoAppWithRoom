package c.gingdev.memoappwithroom.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import c.gingdev.memoappwithroom.R
import kotlinx.android.synthetic.main.activity_logined.*

class LoginedActivty: AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_logined)

		intent.let {
			this.SayHello.text = "안녕하세요 ${it.getStringExtra("Name")}님."
		}
	}
}