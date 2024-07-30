package spaceshootinggame.view

import spaceshootinggame.model.User
import scalafx.scene.control.{TableView, TableColumn, Alert, ButtonType}
import scalafx.beans.property.{StringProperty, IntegerProperty}
import scalafxml.core.macros.sfxml
import scalafx.collections.ObservableBuffer
import javafx.fxml.FXML
import spaceshootinggame.SpaceShootingGame

@sfxml
class ScoreboardController(
                            @FXML private val scoresTable: TableView[User],
                            @FXML private val nameColumn: TableColumn[User, String],
                            @FXML private val scoreColumn: TableColumn[User, Int]
                          ) {

  // Initialize the scores table
  val scores = ObservableBuffer(User.getAllUsers: _*)

  // Bind the columns to the data
  nameColumn.cellValueFactory = { cellData => cellData.value.name }
  scoreColumn.cellValueFactory = { cellData => cellData.value.score.asInstanceOf[scalafx.beans.value.ObservableValue[Int, Int]] }

  // Set the items for the table
  scoresTable.items = scores

  def back(): Unit = {
    SpaceShootingGame.showHomePage()
  }

  def delete(): Unit = {
    val selectedUser = scoresTable.getSelectionModel.getSelectedItem
    if (selectedUser != null) {
      val alert = new Alert(Alert.AlertType.Confirmation) {
        initOwner(SpaceShootingGame.stage)
        title = "Delete Confirmation"
        headerText = "Are you sure you want to delete this score?"
        contentText = s"Player: ${selectedUser.name.value}, Score: ${selectedUser.score.value}"
      }

      val result = alert.showAndWait()

      result match {
        case Some(ButtonType.OK) =>
          // Delete the score from the database
          selectedUser.delete()
          // Remove the score from the table
          scores.remove(selectedUser)
        case _ =>
        // Do nothing if cancel is selected
      }
    } else {
      val alert = new Alert(Alert.AlertType.Warning) {
        initOwner(SpaceShootingGame.stage)
        title = "No Selection"
        headerText = "No Score Selected"
        contentText = "Please select a score to delete."
      }
      alert.showAndWait()
    }
  }
}
