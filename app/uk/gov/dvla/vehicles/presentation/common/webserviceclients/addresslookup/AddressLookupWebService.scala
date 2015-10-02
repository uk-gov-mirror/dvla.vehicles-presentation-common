package uk.gov.dvla.vehicles.presentation.common.webserviceclients.addresslookup

import play.api.i18n.Lang
import play.api.libs.ws.WSResponse
import scala.concurrent.Future
import uk.gov.dvla.vehicles.presentation.common.clientsidesession.TrackingId

// Wrapper around our webservice call so that we can IoC fake versions for testing or use the real version.
trait AddressLookupWebService {

  def callPostcodeWebService(postcode: String, trackingId: TrackingId)
                            (implicit lang: Lang): Future[WSResponse]

  def callAddresses(postcode: String, trackingId: TrackingId)
                            (implicit lang: Lang): Future[WSResponse]

}
