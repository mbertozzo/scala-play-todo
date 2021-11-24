package controllers

import com.google.inject.Inject
import models.{User, UserForm}
import play.api.libs.json.Json
import play.api.mvc._
import services.UserService

import scala.concurrent._
import ExecutionContext.Implicits.global

class UserController @Inject() (val cc: ControllerComponents, val userService: UserService) extends AbstractController(cc) {

  implicit val userFormat = Json.format[User]

  def getAll() = Action.async { implicit request: Request[AnyContent] =>
    userService.listAllUsers map { items =>
      Ok(Json.toJson(items))
    }
  }

}
