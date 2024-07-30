package spaceshootinggame.view

import scalafxml.core.macros.sfxml
import spaceshootinggame.SpaceShootingGame

@sfxml
class RootLayoutController {
  def home(): Unit = {
    SpaceShootingGame.showHomePage()
  }

  def restart(): Unit = {
    SpaceShootingGame.showUserInputPage()
  }

  def exitGame() = {
    System.exit(0)
  }
}