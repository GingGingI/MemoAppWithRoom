package c.gingdev.memoappwithroom.db.Memo

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface MemoDAO {

    @Query("Select * From Memo Where UserId = :userId")
    fun GetMemo(userId: String): Flowable<Memo>

    @Query("Delete From Memo Where UserId = :userId")
    fun DelMemoAsUser(userId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun Insert(memo: Memo)

    @Update
    fun Update(memo: Memo)

    @Delete
    fun Delete(memo: Memo)
}