package watermark

import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
import java.util.ArrayList
import javax.imageio.ImageIO

class WatermarkedImage(
	val image: BufferedImage,
	val watermark: BufferedImage,
	var transparency: Int,
	val fileName: String,
	private val isUseAlpha: Boolean,
	private val method: String,
	private val position: ArrayList<Int>,
	private val color: Color
	) {
		private val file: File

	init {
		file = createWatermarkedImage()
	}

	private fun createWatermarkedImage(): File {
		val image = getWatermarkedImage(method, position)
		val file = File(fileName)

		saveImage(image, file)

		return file
	}

	private fun saveImage(image: BufferedImage, imageFile: File) {
		ImageIO.write(image, "png", imageFile)
	}

	private fun getWatermarkedImage(method: String, position: ArrayList<Int>): BufferedImage{
		var image = BufferedImage(this.image.width, this.image.height, TYPE_INT_RGB)
		if (method == "single") {
			image = createImage(
				image = this.image,
				watermark = this.watermark,
				method = method,
				position = position
			)
		} else if (method == "grid") {
			for (x in 0 until image.width step watermark.width) {
				for (y in 0 until image.height step watermark.height) {
					image = createImage(
						image = this.image,
						watermark = this.watermark,
						method = method,
						position = arrayListOf(x, y)
					)
				}
			}
		}

		return image
	}

	private fun createImage(
		image: BufferedImage,
		watermark: BufferedImage,
		method: String,
		position: ArrayList<Int>
	): BufferedImage {
		val isSingle = method == "single"
		val isGrid = method == "grid"
		var isCanPaint = false
		var dx = 0
		var dy = 0
		var rangeX = 0 until image.width
		var rangeY = 0 until image.height

		if (isGrid) {
			rangeX = position[0] until image.width
			rangeY = position[1] until image.height
		}

		for (x in rangeX) {
			if (isCanPaint) {
				dx++
			}
			else {
				dx = x - position[0]
			}
			for (y in rangeY) {
				if (isCanPaint) {
					dy++
				} else {
					dy = y - position[1]
				}
				if (isSingle || isGrid) {
					isCanPaint = if (x >= position[0] && x < watermark.width + position[0] &&
						y >= position[1] && y < watermark.height + position[1]) {
						dx < watermark.width && dy < watermark.height
					} else false
				}
				var i: Color
				var w: Color
				if (this.isUseAlpha) {
					i = Color(image.getRGB(x, y), true)
					w = if (isCanPaint) {
						Color(watermark.getRGB(dx, dy), true)
					} else {
						Color(image.getRGB(x, y), true)
					}
				} else {
					i = Color(image.getRGB(x, y), false)
					w = if (isCanPaint) {
						Color(watermark.getRGB(dx, dy), false)
					} else {
						Color(image.getRGB(x, y), false)
					}
				}
				if (this.color == w) {
					w = Color(0, 0, 0, 0)
				}
				if (w.alpha == 0) {
					image.setRGB(x, y, this.image.getRGB(x, y))
				} else if (w.alpha == 255) {
					val color = Color(
						(this.transparency * w.red + (100 - this.transparency) * i.red) / 100,
						(this.transparency * w.green + (100 - this.transparency) * i.green) / 100,
						(this.transparency * w.blue + (100 - this.transparency) * i.blue) / 100
					)
					image.setRGB(x, y, color.rgb)
				}
			}
		}

		return image
	}
}