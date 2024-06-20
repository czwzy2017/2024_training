package services

import play.api.libs.json._
import play.api.mvc._
import models._
import play.api.mvc.Results._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class CartService

object CartService {
  val products: List[Product] = List(
    Product("p001", "Scala Book", 39.99),
    Product("p002", "Akka Concurrency", 49.99),
    Product("p003", "Play Framework Guide", 59.99),
    Product("p004", "Scala Test Kit", 29.99),
    Product("p005", "Spark Data Processing", 69.99),
  )
  val users: List[User] = List(
    User("u001", mutable.Map[String, Int]()),
    User("u002", mutable.Map[String, Int]()),
    User("u003", mutable.Map[String, Int]()),
    User("u004", mutable.Map[String, Int]()),
    User("u005", mutable.Map[String, Int]())
  )

  def addProduct(request: Request[JsValue]): Result = {
    request.body.validate[addRequest] match {
      case JsSuccess(addRequest, _) =>
        val user = selectUser(addRequest.userId)
        user.cart(addRequest.productId) = user.cart.getOrElse(addRequest.productId, 0) + addRequest.count
        println(users.toString())
        Created("添加成功")
      case JsError(errors) =>
        BadRequest(Json.obj("错误信息：" -> JsError.toJson(errors)))
    }
  }

  def listCart(userId: String): ListBuffer[Cart] = {
    val carts: ListBuffer[Cart] = new ListBuffer[Cart]()
    val user = selectUser(userId)
    for ((productId, count) <- user.cart) {
      val product = selectProduct(productId)
      carts.append(Cart(product.id, product.name, count))
    }
    carts
  }

  def emptyCart(userId: String): Double = {
    val user = selectUser(userId)
    var total: Double = 0
    for ((productId, count) <- user.cart) {
      val product = selectProduct(productId)
      total += product.price * count
    }
    user.cart.clear()
    total
  }

  private def selectUser(userId: String): User = {
    val userOpt: Option[User] = users.find(_.id == userId)
    userOpt match {
      case Some(user) => user
    }
  }

  private def selectProduct(productId: String): Product = {
    val productOpt: Option[Product] = products.find(_.id == productId)
    productOpt match {
      case Some(product) => product
    }
  }
}