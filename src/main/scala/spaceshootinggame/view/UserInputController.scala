package spaceshootinggame.view

import scalafx.scene.control.{Alert, ButtonType, TextField}
import scalafx.scene.layout.{AnchorPane, BorderPane}
import scalafx.stage.Window
import scalafxml.core.NoDependencyResolver
import scalafxml.core.macros.sfxml
import spaceshootinggame.SpaceShootingGame
import spaceshootinggame.model.User

@sfxml
class UserInputController(private val nameField: TextField) {

  def handleOk(): Unit = {
    if (isInputValid()) {
      // Set the currentUser before navigating to the game page
      User.currentUser = User(nameField.text.value, 0)
      SpaceShootingGame.showGamePage()
    }
  }

  def handleCancel(): Unit = {
    // Navigate back to the home page
    SpaceShootingGame.showHomePage()
  }

  private def isInputValid(): Boolean = {
    var errorMessage = ""

    if (nameField.text.value == null || nameField.text.value.isEmpty) {
      errorMessage += "No valid name!\n"
    }

    if (errorMessage.isEmpty) {
      true
    } else {
      // Show the error message.
      val alert = new Alert(Alert.AlertType.Error) {
        initOwner(SpaceShootingGame.stage)
        title = "Invalid Fields"
        headerText = "Please correct invalid fields"
        contentText = errorMessage
      }.showAndWait()

      false
    }
  }
}
