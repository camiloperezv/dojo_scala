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
    request => val requestBody = request.body.asJson.get

    requestBody.validate[Place] match{
      case success: JsSuccess[Place]=>
        places = places:+ success.get
        Ok(Json.toJson(Map("Responnse"->"Ingresando")))
      case error: JsError => BadRequest(Json.toJson(Map("error"->"error")))
    }
  }

  def updatePlace = Action{
    request => val requestBody = request.body.asJson.get
    requestBody.validate[Place] match{
      case success: JsSuccess[Place]=>
        var newPlace = Place(success.get.id, success.get.name, success.get.descripton)
        places = places.map(x => if(x.id == success.get.id) newPlace else x)
        Ok("done")
      case error: JsError => BadRequest(Json.toJson(Map("error"->"error")))
    }
  }
  def removePlace(id:Int) = Action{
    places = places.filterNot(_.id == id)
    Ok(Json.toJson(Map("Response"->"Eliminado")))
  }

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
}
