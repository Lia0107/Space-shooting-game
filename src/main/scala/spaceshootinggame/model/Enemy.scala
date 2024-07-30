package spaceshootinggame.model

import scalafx.scene.image.{Image, ImageView}

class Enemy(imagePath: String, initialX: Double, initialY: Double, val fallSpeed: Double) extends ImageView {
  image = new Image(getClass.getResourceAsStream(imagePath))
  fitWidth = 50
  fitHeight = 50
  layoutX = initialX
  layoutY = initialY

  def updatePosition(): Unit = {
    layoutY.value += fallSpeed
  }
}
