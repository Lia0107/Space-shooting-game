package spaceshootinggame.view

import spaceshootinggame.SpaceShootingGame
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import scalafxml.core.macros.sfxml

@sfxml
class InformationPageController {


  def back(): Unit = {
    println("Back arrow clicked")
    SpaceShootingGame.showHomePage()
  }
}
