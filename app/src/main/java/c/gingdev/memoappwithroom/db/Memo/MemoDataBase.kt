package c.gingdev.memoappwithroom.db.Memo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Memo::class), version = 1)
abstract class MemoDataBase: RoomDatabase() {

	abstract fun getMemoDAO(): MemoDAO

	companion object {
		private var MEMO_INSTANCE: MemoDataBase? = null

//		a ?: b  -> if(a != null) a else b

		fun getInstance(context: Context): MemoDataBase {
			return MEMO_INSTANCE ?: synchronized(this) {
				MEMO_INSTANCE ?: buildDataBase(context).also { MEMO_INSTANCE = it }
			}
		}

		private fun buildDataBase(context: Context): MemoDataBase
			= Room.databaseBuilder( context.applicationContext,
				MemoDataBase::class.java, "memodb.db").build()
	}
}