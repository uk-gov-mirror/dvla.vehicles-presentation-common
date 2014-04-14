package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.Logger

object SoapEndpointError extends Controller {
  def present = Action { implicit request =>
    Logger.debug(s"SoapEndpointError - displaying the micro service error page")
    Ok(views.html.disposal_of_vehicle.micro_service_error())
  }
}