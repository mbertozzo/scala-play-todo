package controllers

import models.TodoListItem

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._

import scala.collection.mutable

@Singleton
class TodoListController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
 private val todoList = new mutable.ListBuffer[TodoListItem] ()
  todoList += TodoListItem(1, "test", true)
  todoList += TodoListItem(2, "some other value", false)

  implicit val todoListJson = Json.format[TodoListItem]

  def getAll(): Action[AnyContent] = Action {
    if (todoList.isEmpty) {
      NoContent
    } else {
      Ok(Json.toJson(todoList))
    }
  }

  def getById(itemId: Long) = Action {
    val foundItem = todoList.find(_.id == itemId)

    foundItem match {
      case Some(item) => Ok(Json.toJson(item))
      case None => NotFound
    }
  }
}
