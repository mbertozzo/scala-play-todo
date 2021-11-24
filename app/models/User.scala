package models

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms.{boolean, mapping, nonEmptyText}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}


case class User(id: Long, name: String, surname: String, isAdmin: Boolean)

case class UserFormData(name: String, surname: String, isAdmin: Boolean)

object UserForm {
  val form = Form(
    mapping(
      "name" -> nonEmptyText,
      "surname" -> nonEmptyText,
      "isAdmin" -> boolean,
    )(UserFormData.apply)(UserFormData.unapply)
  )
}

class UserTableDef(tag: Tag) extends Table[User](tag, "user") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def name = column[String]("name")

  def surname = column[String]("surname")

  def isAdmin = column[Boolean]("isAdmin")

  override def * = (id, name, surname, isAdmin) <> (User.tupled, User.unapply)
}

class UserList @Inject()(
                          protected val dbConfigProvider: DatabaseConfigProvider
                        )(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  var userList = TableQuery[UserTableDef]

  def add(userItem: User): Future[String] = {
    dbConfig.db
      .run(userList += userItem)
      .map(res => "User successfully added")
      .recover { case ex: Exception => {
        printf(ex.getMessage())
        ex.getMessage
      }
    }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(userList.filter(_.id === id).delete)
  }

  def update(userItem: User): Future[Int] = {
    dbConfig.db
      .run(
        userList
          .filter(_.id === userItem.id)
          .map(x => (x.name, x.surname, x.isAdmin))
          .update(userItem.name, userItem.surname, userItem.isAdmin)
      )
  }

  def get(id: Long): Future[Option[User]] = {
    dbConfig.db.run(userList.filter(_.id === id).result.headOption)
  }

  def getAll: Future[Seq[User]] = {
    dbConfig.db.run(userList.result)
  }

}