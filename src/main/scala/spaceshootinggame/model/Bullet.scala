package spaceshootinggame.model

import scalafx.scene.image.{Image, ImageView}

class Bullet(imagePath: String, initialX: Double, initialY: Double, val moveSpeed: Double) extends ImageView {
  image = new Image(getClass.getResourceAsStream(imagePath))
  fitWidth = 10
  fitHeight = 20
  layoutX = initialX
  layoutY = initialY

  def updatePosition(): Unit = {
    layoutY.value -= moveSpeed
  }
}
