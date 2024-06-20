package services

import akka.actor._
import play.api.libs.json.JsValue
import play.api.mvc._

case class AddProduct(request: Request[JsValue])

case class ListCart(request: Request[AnyContent])

case class EmptyCart(request: Request[AnyContent])

class MyActor extends Actor() {
  def receive: Receive = {
    case AddProduct(request) => sender() ! CartService.addProduct(request)
    case ListCart(userId) => sender() ! CartService.listCart(userId)
    case EmptyCart(userId) => sender() ! CartService.emptyCart(userId)
  }
}

object MyActor {
  def props: Props = Props[MyActor]
}
