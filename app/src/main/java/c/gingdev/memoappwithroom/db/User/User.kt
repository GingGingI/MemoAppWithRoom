package c.gingdev.memoappwithroom.db.User

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class User(
	@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "Key") val Key: Int,
	@ColumnInfo(name = "UserId") val ID: String,
	@ColumnInfo(name = "Password") val Password: String,
	@ColumnInfo(name = "Name") val Name: String
): Serializable