package models
import scala.collection.mutable

case class User(id: String, cart: mutable.Map[String,Int])
