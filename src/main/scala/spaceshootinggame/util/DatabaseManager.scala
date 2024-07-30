package spaceshootinggame.util

import scalikejdbc._
import spaceshootinggame.model.User

import scala.util.{Failure, Success}

object DatabaseManager {

  val derbyDriverClassname = "org.apache.derby.jdbc.EmbeddedDriver"
  val dbURL = "jdbc:derby:myDB;create=true;"

  Class.forName(derbyDriverClassname)
  ConnectionPool.singleton(dbURL, "me", "mine")

  def setupDB(): Unit = {
    implicit val session = AutoSession
    if (!hasDBInitialized) {
      User.initializeTable()
    }
  }

  def hasDBInitialized: Boolean = {
    implicit val session = AutoSession
    DB getTable "user" match {
      case Some(_) => true
      case None => false
    }
  }

  def saveScore(name: String, score: Int): Unit = {
    implicit val session = AutoSession
    val user = User(name, score)
    user.save() match {
      case Success(_) => println(s"Score for $name saved successfully.")
      case Failure(exception) => println(s"Failed to save score: $exception")
    }
  }

  def getAllScores: List[User] = {
    User.getAllUsers
  }

  def clearAllScores(): Unit = {
    User.clearAllUsers()
  }
}
