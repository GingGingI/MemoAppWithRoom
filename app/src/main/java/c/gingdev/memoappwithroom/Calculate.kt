package c.gingdev.memoappwithroom

import kotlin.math.abs

object Calculate {
	fun GetFloatValue(MAX: Float, VALUE: Float): Float
		= if (abs(VALUE) / abs(MAX) > 1) 1.0f else abs(VALUE) / abs(MAX)
}