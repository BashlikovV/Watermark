package watermark

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

class GetWatermark  (private val image: BufferedImage) {
	val watermarkImage: BufferedImage
	var flag: Boolean = true
	var isUseAlpha: Boolean = false
	var color = Color(0, 0, 0)

	init {
		watermarkImage = getWatermark()
	}

	private fun getWatermark(): BufferedImage {
		val console = Scanner(System.`in`)
		var image = BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)

		println("Input the watermark image filename:")
		val fileName = console.nextLine()

		val imageFile = File(fileName)
		if (!imageFile.exists()) {
			flag = false

			println("The file $fileName doesn't exist.")
		} else {
			image = ImageIO.read(imageFile)

			if (this.image.width < image.width || this.image.height < image.height) {
				println("The watermark's dimensions are larger.")
				flag = false
			}

			if (image.colorModel.numColorComponents != 3 && flag) {
				flag = false
				println("The number of watermark color components isn't 3.")
			}

			if (image.colorModel.componentSize[0] != 8 && flag) {
				flag = false
				println("The watermark isn't 24 or 32-bit.")
			}

			if (image.colorModel.hasAlpha() && flag) {
				println("Do you want to use the watermark's Alpha channel?")

				if (console.nextLine().lowercase(Locale.getDefault()) == "yes") {
					isUseAlpha = true
				}
			} else if (flag) {
				println("Do you want to set a transparency color?")

				if (console.nextLine() == "yes") {
					println("Input a transparency color ([Red] [Green] [Blue]):")

					var red = -1
					var green = -1
					var blue = -1

					var counter = 0
					val colorsStr = console.nextLine()
					val tmp = arrayListOf<Char>()

					if (colorsStr.isEmpty()) {
						flag = false
					} else {
						for (i in colorsStr.indices) {
							tmp.add(colorsStr[i])

							if (colorsStr[i] == ' ' || i == colorsStr.length - 1) {
								counter++

								try {
									when (counter) {
										1 -> red = tmp.toString().filter { it.isDigit() || it == '-'}.toInt()
										2 -> green = tmp.toString().filter { it.isDigit() || it == '-'}.toInt()
										3 -> blue = tmp.toString().filter { it.isDigit() || it == '-'}.toInt()
									}
									tmp.clear()
								} catch (e: Exception) {
									println("The transparency color input is invalid.")
									flag = false
								}
							}
							if (counter > 3) {
								println("The transparency color input is invalid.")
								flag = false
							}
						}
					}

					if (flag) {
						val colorRange = 0..255
						if (red in colorRange && green in colorRange && blue in colorRange) {
							color = Color(red, green, blue)
						} else {
							println("The transparency color input is invalid.")

							this.flag = false
						}
					}

				}
			}
		}
		return image
	}

//	private fun checkCorrect(): Boolean {
//		if (this.watermarkImage.width != this.image.width || this.watermarkImage.height != this.image.height) {
//			println("The image and watermark dimensions are different.")
//			return false
//		}
//		return true
//	}
}