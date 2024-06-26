package services

import akka.actor._
import play.api.libs.json._
import play.api.mvc._
import models._
import play.api.mvc.Results._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

case class AddProduct(request: Request[JsValue])

case class ListCart(request: Request[AnyContent])

case class EmptyCart(request: Request[AnyContent])

class CartService extends Actor() {
  def receive: Receive = {
    case AddProduct(request) =>
      val result = request.body.validate[addRequest] match {
        case JsSuccess(addRequest, _) =>
          val user = selectUser(addRequest.userId)
          user.cart(addRequest.productId) = user.cart.getOrElse(addRequest.productId, 0) + addRequest.count
          println(CartService.users.toString())
          Created("添加成功")
        case JsError(errors) =>
          BadRequest(Json.obj("错误信息：" -> JsError.toJson(errors)))
      }
      sender() ! result
    case ListCart(request) =>
      val userIdOpt = request.getQueryString("userId")
      val result = userIdOpt match {
        case Some(userId) =>
          val carts: ListBuffer[Cart] = new ListBuffer[Cart]()
          val user = selectUser(userId)
          for ((productId, count) <- user.cart) {
            val product = selectProduct(productId)
            carts.append(Cart(product.id, product.name, count))
          }
          Ok("购物车：" + carts.toString())
        case None => BadRequest("未传入userId")
      }
      sender() ! result
    case EmptyCart(request) =>
      val userIdOpt = request.getQueryString("userId")
      val result = userIdOpt match {
        case Some(userId) =>
          val user = selectUser(userId)
          var total: Double = 0
          for ((productId, count) <- user.cart) {
            val product = selectProduct(productId)
            total += product.price * count
          }
          user.cart.clear()
          Ok("已清空购物车，购车中所有商品总价为：" + total)
        case None => BadRequest("未传入userId")
      }
      sender() ! result
  }

  private def selectUser(userId: String): User = {
    val userOpt: Option[User] = CartService.users.find(_.id == userId)
    userOpt match {
      case Some(user) => user
    }
  }

  private def selectProduct(productId: String): Product = {
    val productOpt: Option[Product] = CartService.products.find(_.id == productId)
    productOpt match {
      case Some(product) => product
    }
  }
}

object CartService {
  def props: Props = Props[CartService]

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
}
