package services

import com.google.inject.Inject
import models.{User, UserList}

import scala.concurrent.Future

class UserService @Inject() (users: UserList) {
  def addUser(user: User): Future[String] = {
    users.add(user)
  }

  def deleteUser(id: Long): Future[Int] = {
    users.delete(id)
  }

  def updateUser(user: User): Future[Int] = {
    users.update(user)
  }

  def getUser(id: Long): Future[Option[User]] = {
    users.get(id)
  }

  def listAllUsers: Future[Seq[User]] = {
    users.getAll
  }
}
