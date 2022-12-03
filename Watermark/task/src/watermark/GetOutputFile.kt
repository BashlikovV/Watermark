package watermark

import java.awt.image.BufferedImage
import java.util.ArrayList
import java.util.Scanner

class GetOutputFile(private val image: BufferedImage, private val watermark: BufferedImage) {
	var name: String = ""
	val method: String
	val position = arrayListOf(0 , 0)
	var flag = true

	init {
		method = getPosMethod()

		if (flag) {
			if (method == "single") {
				val tmp = getPos()
				try {
					position[0] = tmp[0]
					position[1] = tmp[1]
				} catch (e: Exception) {
					flag = false
				}
			}

		}
		if (flag) {
			name = getFileName()
		}
		if (flag) println("The watermarked image $name has been created.")
	}

	private fun getFileName(): String {
		val console = Scanner(System.`in`)

		println("Input the output image filename (jpg or png extension):")
		val name = console.nextLine()

		if (name.substringAfter('.') != "jpg" && name.substringAfter('.') != "png") {
			flag = false
			println("The output file extension isn't \"jpg\" or \"png\".")
		}
		return name
	}

	private fun getPosMethod(): String {
		val console = Scanner(System.`in`)
		println("Choose the position method (single, grid):")
		val method = console.nextLine()

		if (method != "single" && method != "grid") {
			flag = false
			println("The position method input is invalid.")
		}
		return method
	}

	private fun getPos(): ArrayList<Int> {
		val console = Scanner(System.`in`)
		val result = ArrayList<Int>()
		val tmp = ArrayList<Char>()

		println("Input the watermark position " +
				"([x 0-${this.image.width - this.watermark.width}] " +
				"[y 0-${this.image.height - this.watermark.height}]): ")
		val posStr = console.nextLine()

		var counter = 0
		for (i in posStr.indices) {
			tmp.add(posStr[i])

			if (posStr[i] == ' ' || i == posStr.length - 1) {
				counter++

				try {
					when (counter) {
						1 -> result.add(tmp.toString().filter { it.isDigit() || it == '-'}.toInt())
						2 -> result.add(tmp.toString().filter { it.isDigit() || it == '-'}.toInt())
					}
					tmp.clear()
				} catch (e: Exception) {
					println("The position input is invalid.")
					flag = false
				}
			}
			if (counter > 2) {
				println("The position input is invalid.")
				flag = false
			}
		}

		if (flag) {
			flag = try {
				result[0] in 0..this.image.width - this.watermark.width &&
						result[1] in 0..this.image.height - this.watermark.height
			} catch (e: Exception) {
				false
			}
			if (!flag) {
				println("The position input is out of range.")
			}
		}

		return result
	}
}