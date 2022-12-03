package watermark

import java.util.Scanner

class GetTransparency {
	val transparency: Int
	var flag = true

	init {
		transparency = getInTransparency()
	}

	private fun getInTransparency(): Int {
		val console = Scanner(System.`in`)
		val tmp: String
		var num = 0

		println("Input the watermark transparency percentage (Integer 0-100):")
		try {
			tmp = console.next()
			num = tmp.toInt()

			if (num > 100) {
				flag = false
				println("The transparency percentage is out of range.")
			}
		} catch (e: Exception) {
			flag = false
			println("The transparency percentage isn't an integer number.")
		}

		return num
	}
}