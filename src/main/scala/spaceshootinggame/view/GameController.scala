package spaceshootinggame.view

import spaceshootinggame.model.{Bullet, Enemy, Spaceship, User}
import spaceshootinggame.util.DatabaseManager
import scalafx.scene.input.KeyEvent
import scalafx.scene.layout.Pane
import scalafx.scene.control.{Alert, ButtonType, Label}
import scalafx.animation.AnimationTimer
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.scene.text.Font
import scalafx.scene.paint.Color
import scalafx.scene.media.AudioClip
import javafx.fxml.FXML
import scalafx.application.Platform
import spaceshootinggame.SpaceShootingGame

import scala.collection.mutable.ListBuffer

@sfxml
class GameController(private val gamePane: Pane, @FXML private val scoreLabel: Label, @FXML private val timerLabel: Label) {

  private val spaceship = new Spaceship("/images/spaceship.png", (1280 - 50) / 2, 720 - 50 - 20, 20)
  private val shootSound = new AudioClip(getClass.getResource("/sounds/lasersound.mp3").toString)
  private var bullets = ListBuffer[Bullet]()
  private var enemies = ListBuffer[Enemy]()
  private var missedEnemies = 0
  private var score = 0
  private val gameDuration = 60000 // Game duration in milliseconds (e.g., 60,000 ms = 60 seconds)
  private var gameOver = false // Flag to check if the game is over
  private var timer: AnimationTimer = _

  scoreLabel.setFont(Font.font("Arial", 24))
  scoreLabel.setTextFill(Color.White) // Set the text color to white

  timerLabel.setFont(Font.font("Arial", 24))
  timerLabel.setTextFill(Color.White) // Set the text color to white

  gamePane.children.add(spaceship)
  setupKeyHandlers()
  startGameLoop()

  private def setupKeyHandlers(): Unit = {
    gamePane.onKeyPressed = (event: KeyEvent) => {
      event.code.getName match {
        case "Up" => spaceship.moveUp()
        case "Down" => spaceship.moveDown(gamePane.height.value)
        case "Left" => spaceship.moveLeft()
        case "Right" => spaceship.moveRight(gamePane.width.value)
        case "Space" => shoot()
        case _ =>
      }
    }
    gamePane.requestFocus()
  }

  private def shoot(): Unit = {
    if (!gameOver) {
      val bullet = new Bullet("/images/laser.png", spaceship.layoutX.value + spaceship.fitWidth.value / 2 - 5, spaceship.layoutY.value - 20, 5)
      bullets += bullet
      gamePane.children.add(bullet)
      shootSound.play()
    }
  }

  private def spawnEnemy(): Unit = {
    if (!gameOver) {
      // Use the dimensions of the background image (1280x720) for spawning enemies
      val enemy = new Enemy("/images/alien1.png", math.random * (1280 - 50), 0, 1.0)
      enemies += enemy
      gamePane.children.add(enemy)
    }
  }

  private def startGameLoop(): Unit = {
    val startTime = System.currentTimeMillis()
    timer = AnimationTimer { _ =>
      if (!gameOver) {
        val elapsedTime = System.currentTimeMillis() - startTime
        val remainingTime = (gameDuration - elapsedTime) / 1000
        timerLabel.text = s"Time: $remainingTime"
        updateBullets()
        updateEnemies()
        checkCollisions()
        if (missedEnemies > 4) {
          gameOver = true
          timer.stop() // Stop the timer before showing the dialog
          showGameOverDialog("You missed too many enemies!")
        }
        if (elapsedTime >= gameDuration) {
          gameOver = true
          timer.stop() // Stop the timer before showing the dialog
          showGameOverDialog("Time is up!")
        }
      }
    }
    timer.start()
  }

  private def updateBullets(): Unit = {
    bullets.foreach(_.updatePosition())
    bullets.filter(_.layoutY.value <= 0).foreach { bullet =>
      gamePane.children.remove(bullet)
    }
    bullets = bullets.filter(_.layoutY.value > 0)
  }

  private def updateEnemies(): Unit = {
    enemies.foreach(_.updatePosition())
    enemies.filter(_.layoutY.value > 720).foreach { enemy =>
      missedEnemies += 1
      println(s"Missed enemies: $missedEnemies") // Debugging statement
      gamePane.children.remove(enemy)
      if (missedEnemies > 4 && !gameOver) {
        gameOver = true
        timer.stop()
        showGameOverDialog("You missed too many enemies!")
      }
    }
    enemies = enemies.filter(_.layoutY.value <= 720)
    if (math.random < 0.01) spawnEnemy()
  }

  private def checkCollisions(): Unit = {
    val bulletsToRemove = ListBuffer[Bullet]()
    val enemiesToRemove = ListBuffer[Enemy]()

    bullets.foreach { bullet =>
      enemies.foreach { enemy =>
        if (bullet.boundsInParent.value.intersects(enemy.boundsInParent.value)) {
          bulletsToRemove += bullet
          enemiesToRemove += enemy
          updateScore(10)
        }
      }
    }

    bulletsToRemove.foreach { bullet =>
      bullets -= bullet
      gamePane.children.remove(bullet)
    }

    enemiesToRemove.foreach { enemy =>
      enemies -= enemy
      gamePane.children.remove(enemy)
    }
  }

  private def updateScore(points: Int): Unit = {
    score += points
    scoreLabel.text = s"Score: $score"
  }

  def showGameOverDialog(reason: String): Unit = {
    val returnHomeButton = new ButtonType("Return HomePage")
    val closeButton = new ButtonType("Close")

    Platform.runLater {
      val alert = new Alert(Alert.AlertType.Information) {
        initOwner(gamePane.scene().getWindow)
        title = "Game Over"
        headerText = s"Game Over\nFinal Score: $score"
        contentText = s"$reason\nThank you for playing!"
        buttonTypes = Seq(returnHomeButton, closeButton)
      }

      val result = alert.showAndWait()

      result match {
        case Some(`returnHomeButton`) =>
          println("Return to Home Page clicked")
          DatabaseManager.saveScore(User.currentUser.name.value, score)
          SpaceShootingGame.showHomePage()
        case Some(`closeButton`) =>
          println("Close clicked")
          DatabaseManager.saveScore(User.currentUser.name.value, score)
          SpaceShootingGame.showHomePage()
        case _ =>
          println("Dialog closed without selecting a button")
      }
    }
  }
}
