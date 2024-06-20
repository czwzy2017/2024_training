package controllers

import play.api.libs.json._
import services._
import play.api.mvc._
import models._

import javax.inject.Inject

class CartController @Inject()(controllerComponents: ControllerComponents) extends AbstractController(controllerComponents) {
  def addProduct: Action[JsValue] = Action(parse.json) { implicit request: Request[JsValue] =>
    request.body.validate[addRequest] match {
      case JsSuccess(addRequest, _) =>
        CartService.addProduct(addRequest.userId, addRequest.productId, addRequest.count)
        Created("添加成功")
      case JsError(errors) =>
        BadRequest(Json.obj("错误信息：" -> JsError.toJson(errors)))
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
