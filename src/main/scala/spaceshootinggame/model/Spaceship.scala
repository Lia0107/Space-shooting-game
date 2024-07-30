package spaceshootinggame.model
import scalafx.scene.image.{Image, ImageView}

class Spaceship(imagePath: String, initialX: Double, initialY: Double, val moveSpeed: Double) extends ImageView {
  image = new Image(getClass.getResourceAsStream(imagePath))
  fitWidth = 50
  fitHeight = 50
  layoutX = initialX
  layoutY = initialY

  def moveUp(): Unit = {
    if (layoutY.value - moveSpeed >= 0) {
      layoutY.value -= moveSpeed
    }
  }

  def moveDown(boundary: Double): Unit = {
    if (layoutY.value + moveSpeed + fitHeight.value <= boundary) {
      layoutY.value += moveSpeed
    }
  }

  def moveLeft(): Unit = {
    if (layoutX.value - moveSpeed >= 0) {
      layoutX.value -= moveSpeed
    }
  }

  def moveRight(boundary: Double): Unit = {
    if (layoutX.value + moveSpeed + fitWidth.value <= boundary) {
      layoutX.value += moveSpeed
    }
  }
}
