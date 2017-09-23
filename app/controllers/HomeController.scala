package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import models.Place
import play.api.libs.json.{JsError, JsSuccess, Json}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  var places = List(
    Place(1, "Barbosa", Some("cualquier cosa")),
    Place(2, "udea", Some("otra cosa")),
    Place(3, "envigado", None),
  )

  def listPlaces = Action{
    val json = Json.toJson(places)
    Ok(json)
  }

  def addPlace = Action{
    request => val json = request.body.asJson.get

    json.validate[Place] match{
      case success: JsSuccess[Place]=>
        places = places:+ success.get
        Ok("OK")
      case error: JsError => BadRequest(Json.toJson(Map("error"->"error")))
    }
  }

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
}
