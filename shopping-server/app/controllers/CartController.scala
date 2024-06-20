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

  def listCart = Action.async { implicit request: Request[AnyContent] =>
    (myActor ? ListCart(request)).mapTo[Result].map{ result=>
      result
    }
  }

  def emptyCart = Action.async { implicit request: Request[AnyContent] =>
    (myActor ? EmptyCart(request)).mapTo[Result].map{ result=>
      result
    }
  }
}
