package c.gingdev.memoappwithroom.db.User

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface UserDAO {

	@Query("Select * From User Where UserId = :Id And Password = :Pw")
	fun Login(Id: String, Pw: String): Flowable<User?>

	@Query("Select * From User Where `Key` = :key")
	fun getUserByKey(key: String): Flowable<User>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun Insert(vararg user: User): Completable

	@Delete
	fun Delete(vararg user: User): Completable

	@Update
	fun Update(vararg user: User)
}