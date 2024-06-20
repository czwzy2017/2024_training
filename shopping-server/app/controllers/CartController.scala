package controllers

import play.api.libs.json._
import services._
import play.api.mvc._
import akka.actor._
import akka.pattern.ask
import akka.util.Timeout

import javax.inject.Inject
import scala.concurrent._
import scala.concurrent.duration.DurationInt

class CartController @Inject()(controllerComponents: ControllerComponents)(implicit system: ActorSystem,ec: ExecutionContext) extends AbstractController(controllerComponents) {
  implicit val timeout:Timeout = 5.seconds
  private val myActor:ActorRef = system.actorOf(MyActor.props,"myActor")
  def addProduct: Action[JsValue] = Action(parse.json).async { implicit request: Request[JsValue] =>
    (myActor ? AddProduct(request)).mapTo[Result].map{ result=>
        result
    }
  }

  def listCart = Action { implicit request: Request[AnyContent] =>
    val userIdOpt = request.getQueryString("userId")
    userIdOpt match {
      case Some(userId) => Ok("购物车：" + CartService.listCart(userId).toString())
      case None => BadRequest("未传入userId")
    }
  }

  def emptyCart = Action { implicit request: Request[AnyContent] =>
    val userIdOpt = request.getQueryString("userId")
    userIdOpt match {
      case Some(userId) => Ok("已清空购物车，购车中所有商品总价为：" + CartService.emptyCart(userId))
      case None => BadRequest("未传入userId")
    }
  }
}
