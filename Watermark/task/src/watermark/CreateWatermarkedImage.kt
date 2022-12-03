package watermark

import java.awt.Color
import java.awt.image.BufferedImage
import java.util.ArrayList

class CreateWatermarkedImage {
	private var image: BufferedImage = BufferedImage(1, 1, 1)
	private var watermark: BufferedImage = BufferedImage(1, 1, 1)
	private var transparency: Int = 0
	private var outputFile: String = ""
	private var flag = false

	init {
		createWatermarkedImage()
	}

	private fun createWatermarkedImage() {
		val properties = GetProperties()
		val image = properties.image
		val watermarkProperties: GetWatermark
		val watermark: BufferedImage
		val color: Color
		val transparency: Int
		val fileName: String
		val method: String
		val position: ArrayList<Int>

		if (properties.flag) {
			this.image = image
			watermarkProperties = GetWatermark(image)

			if (watermarkProperties.flag) {
				watermark = watermarkProperties.watermarkImage
				this.watermark = watermark
				color = watermarkProperties.color
				val getTransparency = GetTransparency()

				if (getTransparency.flag) {
					transparency = getTransparency.transparency
					this.transparency = transparency

					val getOutputFile = GetOutputFile(
						image = this.image,
						watermark = this.watermark
					)
					if (getOutputFile.flag) {
						fileName = getOutputFile.name
						method = getOutputFile.method
						position = getOutputFile.position
						this.outputFile = fileName
					} else return
				} else return
			} else return
		} else return

		WatermarkedImage(
			image = image,
			watermark = watermark,
			transparency = transparency,
			fileName = fileName,
			isUseAlpha = watermarkProperties.isUseAlpha,
			method = method,
			position = position,
			color = color
		)

		this.flag = true
	}
}