package uk.gov.dvla.vehicles.presentation.common.webserviceclients.addresslookup.ordnanceservey

import com.google.inject.Inject
import play.api.Logger
import play.api.i18n.Lang
import play.api.libs.ws.{Response, WS}
import uk.gov.dvla.vehicles.presentation.common.LogFormats
import uk.gov.dvla.vehicles.presentation.common.clientsidesession.ClientSideSessionFactory
import uk.gov.dvla.vehicles.presentation.common.webserviceclients.HttpHeaders
import uk.gov.dvla.vehicles.presentation.common.webserviceclients.addresslookup.AddressLookupWebService
import uk.gov.dvla.vehicles.presentation.common.webserviceclients.config.OrdnanceSurveyConfig
import scala.concurrent.Future

final class WebServiceImpl @Inject()(config: OrdnanceSurveyConfig) extends AddressLookupWebService {
  private val baseUrl: String = config.baseUrl
  private val requestTimeout: Int = config.requestTimeout

  override def callPostcodeWebService(postcode: String, trackingId: String)
                                     (implicit lang: Lang): Future[Response] = {
    val endPoint = s"$baseUrl/postcode-to-address?" +
      postcodeParam(postcode) +
      languageParam +
      trackingIdParam(trackingId)

    val postcodeToLog = LogFormats.anonymize(postcode)

    Logger.debug(s"Calling ordnance-survey postcode lookup micro-service with $postcodeToLog") // $endPoint...")
    WS.url(endPoint).
      withHeaders(HttpHeaders.TrackingId -> trackingId).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds
      get()
  }

  override def callUprnWebService(uprn: String, trackingId: String)
                                 (implicit lang: Lang): Future[Response] = {
    val endPoint = s"$baseUrl/uprn-to-address?" +
      s"uprn=$uprn" +
      languageParam +
      trackingIdParam(trackingId)

    val uprnToLog = LogFormats.anonymize(uprn)

    Logger.debug(s"Calling ordnance-survey uprn lookup micro-service with $uprnToLog")
    WS.url(endPoint).
      withHeaders(HttpHeaders.TrackingId -> trackingId).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds
      get()
  }

  def postcodeWithNoSpaces(postcode: String): String = postcode.filter(_ != ' ')

  private def postcodeParam(postcode: String) = s"postcode=${postcodeWithNoSpaces(postcode)}"

  private def trackingIdParam(trackingId: String): String =
    s"&${ClientSideSessionFactory.TrackingIdCookieName}=$trackingId"

  private def languageParam(implicit lang: Lang) = s"&languageCode=${lang.code.toUpperCase}"
}