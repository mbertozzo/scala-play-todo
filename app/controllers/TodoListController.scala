package controllers

import models.{NewTodoListItem, TodoListItem}

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._

import scala.collection.mutable

@Singleton
class TodoListController @Inject()(val controllerComponents: ControllerComponents) extends BaseController with Logging {
 private val todoList = new mutable.ListBuffer[TodoListItem] ()
  todoList += TodoListItem(1, "test", true)
  todoList += TodoListItem(2, "some other value", false)

  implicit val todoListJson = Json.format[TodoListItem]
  implicit val newTodoListJson = Json.format[NewTodoListItem]

  def getAll(): Action[AnyContent] = Action {
    logger.info("Received request for all todos")

    if (todoList.isEmpty) {
      NoContent
    } else {
      Ok(Json.toJson(todoList))
    }
  }

  def getById(itemId: Long): Action[AnyContent] = Action {
    logger.info(s"Received request for todo with id: $itemId")

    val foundItem = todoList.find(_.id == itemId)

    foundItem match {
      case Some(item) => Ok(Json.toJson(item))
      case None => NotFound
    }
  }

  def addNewItem(): Action[AnyContent] = Action { implicit request =>

    val content = request.body
    val jsonObject = content.asJson
    val todoListItem: Option[NewTodoListItem] =
      jsonObject.flatMap(
        Json.fromJson[NewTodoListItem](_).asOpt
      )

    val input = jsonObject.getOrElse("")
    logger.info(s"Received request to add todo: $input")

    todoListItem match {
      case Some(newItem) =>
        val nextId = todoList.map(_.id).max + 1
        val toBeAdded = TodoListItem(nextId, newItem.description, false)
        todoList += toBeAdded
        Created(Json.toJson(toBeAdded))
      case None =>
        BadRequest
    }

  }

  def markAsDone(itemId: Long) = Action {
    logger.info(s"Received request to mark as done todo with id: $itemId")
    val foundItemIndex = todoList.indexWhere(_.id == itemId)

    foundItemIndex match {
      case index if index >= 0 =>

        // val updatedItem = TodoListItem(itemId, item.description, isItDone = true)
        // todoList -= item
        // todoList += updatedItem

        val updatedItem = todoList(foundItemIndex).copy(isItDone = true)
        todoList.update(foundItemIndex, updatedItem)
        Ok(Json.toJson(updatedItem))
      case _ => BadRequest
    }
  }

  def deleteAllDone(): Action[AnyContent] = Action {
    todoList.filterInPlace(_.isItDone == false)
    Ok(Json.toJson(todoList))
  }
}
