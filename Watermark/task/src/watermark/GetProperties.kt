package watermark

import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.*
import java.io.File
import java.util.*
import javax.imageio.ImageIO

class GetProperties {
	val image: BufferedImage
	var flag: Boolean = true

	init {
		image = getProperty()
	}

	private fun getProperty(): BufferedImage {
		val console = Scanner(System.`in`)
		var image = BufferedImage(1, 1, TYPE_INT_RGB)

		println("Input the image filename:")
		val fileName = console.nextLine()
		//val root = System.getProperty("user.dir") + "/Watermark/task/"

		val imageFile = File(fileName)
		if (!imageFile.exists()) {
			flag = false

			println("The file $fileName doesn't exist.")
		} else {
			image = ImageIO.read(imageFile)

			if (image.colorModel.numColorComponents != 3) {
				flag = false
				println("The number of image color components isn't 3.")
			}

			if (image.colorModel.componentSize[0] != 8) {
				flag = false
				println("The image isn't 24 or 32-bit.")
			}
		}
		return image
	}
}