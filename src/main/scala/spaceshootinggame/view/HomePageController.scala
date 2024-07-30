package spaceshootinggame.view

import scalafxml.core.macros.sfxml
import spaceshootinggame.SpaceShootingGame

@sfxml
class HomePageController {

  def handleStartGame(): Unit = {
    SpaceShootingGame.showUserInputPage()
  }

  def handleScoreboard(): Unit = {
    SpaceShootingGame.showScoreboardPage()
  }

  def handleInfo(): Unit = {
    SpaceShootingGame.showInfoPage()
  }
}
