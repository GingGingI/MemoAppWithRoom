package c.gingdev.memoappwithroom.db.Memo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import c.gingdev.memoappwithroom.db.User.User
import java.util.*

@Entity
class Memo(
    @PrimaryKey @ColumnInfo(name = "MemoId") val MemoId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "MemoTitle") val MemoTitle: String,
    @ColumnInfo(name = "MemoContent") val MemoContent: String,
    @ColumnInfo(name = "UserId") val UserId: String
)