package c.gingdev.memoappwithroom.db.Memo

import androidx.paging.DataSource
import androidx.room.*
import io.reactivex.Completable

@Dao
interface MemoDAO {

    @Query("Select * From Memo Where UserId = :userId Order By MemoDate DESC")
    fun GetMemo(userId: String): DataSource.Factory<Int, Memo>

    @Query("Delete From Memo Where UserId = :userId")
    fun DelMemoAsUser(userId: String): Completable

		@Delete
		fun Delete(memo: Memo): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun Insert(memo: Memo): Completable

    @Update
    fun Update(memo: Memo): Completable
}