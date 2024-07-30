package spaceshootinggame

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import javafx.{scene => jfxs}
import scalafx.scene.image.Image
import spaceshootinggame.util.DatabaseManager
import scalafx.scene.media.{Media, MediaPlayer}

object SpaceShootingGame extends JFXApp {

  // Initialize the database
  DatabaseManager.setupDB()

  val rootResource = getClass.getResource("/RootLayout.fxml")
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load()
  val roots = loader.getRoot[jfxs.layout.BorderPane]

  // Background music
  var sharedMusicPlayer: MediaPlayer = _
  var gameMusicPlayer: MediaPlayer = _

  stage = new PrimaryStage {
    title = "Space Shooting"
    icons.add(new Image(getClass.getResourceAsStream("/images/icon/comets.png")))
    scene = new Scene {
      root = roots
    }
  }

  def showHomePage(): Unit = {
    println("Showing Home Page")
    stopGameMusic()
    startSharedMusic()
    val resource = getClass.getResource("/HomePage.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val root = loader.getRoot[jfxs.layout.AnchorPane]
    roots.setCenter(root)
    root.requestFocus() // Ensure the home page root pane has focus
  }

  def showGamePage(): Unit = {
    println("Showing Game Page")
    stopSharedMusic()
    val resource = getClass.getResource("/game.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val root = loader.getRoot[jfxs.layout.AnchorPane]
    roots.setCenter(root)
    root.requestFocus() // Ensure the game page root pane has focus
    playGameMusic()
  }

  def showScoreboardPage(): Unit = {
    println("Showing Scoreboard Page")
    stopGameMusic()
    startSharedMusic()
    val resource = getClass.getResource("/Scoreboard.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val root = loader.getRoot[jfxs.layout.AnchorPane]
    roots.setCenter(root)
    root.requestFocus() // Ensure the scoreboard page root pane has focus
  }

  def showUserInputPage(): Unit = {
    println("Showing User Input Page")
    stopGameMusic()
    startSharedMusic()
    val resource = getClass.getResource("/UserInput.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val root = loader.getRoot[jfxs.layout.AnchorPane]
    roots.setCenter(root)
    root.requestFocus() // Ensure the user input page root pane has focus
  }

  def showInfoPage(): Unit = {
    println("Showing Information Page")
    stopGameMusic()
    startSharedMusic()
    val resource = getClass.getResource("/InformationPage.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val root = loader.getRoot[jfxs.layout.AnchorPane]
    roots.setCenter(root)
    root.requestFocus() // Ensure the scoreboard page root pane has focus
  }

  // Background music methods
  def startSharedMusic(): Unit = {
    if (sharedMusicPlayer == null) {
      val musicResource = getClass.getResource("/sounds/bgm.mp3")
      if (musicResource != null) {
        sharedMusicPlayer = new MediaPlayer(new Media(musicResource.toString))
        sharedMusicPlayer.setCycleCount(MediaPlayer.Indefinite) // Loop the music
        println("Starting shared music")
        sharedMusicPlayer.play()
      }
    } else {
      println("Resuming shared music")
      sharedMusicPlayer.play()
    }
  }

  def stopSharedMusic(): Unit = {
    if (sharedMusicPlayer != null) {
      println("Stopping shared music")
      sharedMusicPlayer.stop()
    }
  }

  def playGameMusic(): Unit = {
    val musicResource = getClass.getResource("/sounds/gpm.mp3")
    if (musicResource != null) {
      gameMusicPlayer = new MediaPlayer(new Media(musicResource.toString))
      gameMusicPlayer.setCycleCount(MediaPlayer.Indefinite) // Loop the music
      println("Starting game music")
      gameMusicPlayer.play()
    }
  }

  def stopGameMusic(): Unit = {
    if (gameMusicPlayer != null) {
      println("Stopping game music")
      gameMusicPlayer.stop()
    }
  }

  // Show the home page initially
  showHomePage()
}
