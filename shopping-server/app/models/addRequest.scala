package models

import play.api.libs.json.{Json, OFormat}

case class addRequest(userId: String, productId: String, count: Int)

object addRequest {
  implicit val addRequestFormat: OFormat[addRequest] = Json.format[addRequest]
}
