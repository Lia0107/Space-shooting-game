package spaceshootinggame.model

import scalafx.beans.property.{StringProperty, IntegerProperty}
import scala.util.{Try, Success, Failure}
import scalikejdbc._
import spaceshootinggame.util.DatabaseManager

class User(val nameS: String, val scoreI: Int) {
  def this() = this(null, 0)

  var name = new StringProperty(nameS)
  var score = IntegerProperty(scoreI)

  def save(): Try[Int] = {
    if (!isExist) {
      Try(DB.autoCommit { implicit session =>
        sql"""
          insert into "user" (name, score) values
          (${name.value}, ${score.value})
        """.update.apply()
      })
    } else {
      Try(DB.autoCommit { implicit session =>
        sql"""
          update "user"
          set
          score = ${score.value}
          where name = ${name.value}
        """.update.apply()
      })
    }
  }

  def delete(): Try[Int] = {
    if (isExist) {
      Try(DB.autoCommit { implicit session =>
        sql"""
          delete from "user" where
          name = ${name.value}
        """.update.apply()
      })
    } else
      throw new Exception("User not exists in Database")
  }

  def isExist: Boolean = {
    DB.readOnly { implicit session =>
      sql"""
        select * from "user" where
        name = ${name.value}
      """.map(rs => rs.string("name")).single.apply()
    } match {
      case Some(_) => true
      case None => false
    }
  }
}

object User {
  var currentUser: User = _

  def apply(nameS: String, scoreI: Int): User = {
    new User(nameS, scoreI)
  }

  def initializeTable(): Unit = {
    DB.autoCommit { implicit session =>
      sql"""
        create table "user" (
          id int not null GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
          name varchar(255),
          score int
        )
      """.execute.apply()
    }
  }

  def getAllUsers: List[User] = {
    DB.readOnly { implicit session =>
      sql"""select * from "user"""".map(rs => User(rs.string("name"), rs.int("score"))).list.apply()
    }
  }

  def clearAllUsers(): Unit = {
    DB.autoCommit { implicit session =>
      sql"""delete from "user"""".update.apply()
    }
  }
}
