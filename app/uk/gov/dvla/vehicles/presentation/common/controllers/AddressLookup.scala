package uk.gov.dvla.vehicles.presentation.common.controllers

import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{Request, Action, Controller}
import uk.gov.dvla.vehicles.presentation.common.clientsidesession.ClientSideSessionFactory
import uk.gov.dvla.vehicles.presentation.common.webserviceclients.addresslookup.AddressLookupService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.control.NonFatal
import uk.gov.dvla.vehicles.presentation.common.clientsidesession.CookieImplicits.RichCookies


abstract class AddressLookup(implicit clientSideSessionFactory: ClientSideSessionFactory,
                             addressLookup: AddressLookupService) extends Controller {

  def byPostcode(postCode: String) = Action.async { request =>
    if (!authenticate(request)) Future.successful(Unauthorized(""))
    else {
      val session = clientSideSessionFactory.getSession(request.cookies)
      addressLookup.addresses(postCode, session.trackingId).map { addressLines =>
        Ok(Json.toJson(addressLines))
      } recover {
        case NonFatal(e) =>
          Logger.warn(s"${e.getMessage} ${e.getStackTraceString} - trackingId: ${request.cookies.trackingId()}")
          InternalServerError(e.getMessage)
      }
    }
  }

  protected def authenticate(request: Request[_]): Boolean
}
