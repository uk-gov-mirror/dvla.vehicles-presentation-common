package uk.gov.dvla.vehicles.presentation.common.webserviceclients.addresslookup.ordnanceservey

import javax.inject.Inject
import play.api.Logger
import play.api.i18n.Lang
import play.api.libs.ws.WSResponse
import uk.gov.dvla.vehicles.presentation.common.LogFormats
import uk.gov.dvla.vehicles.presentation.common.LogFormats.DVLALogger
import uk.gov.dvla.vehicles.presentation.common.clientsidesession.TrackingId
import uk.gov.dvla.vehicles.presentation.common.model.AddressModel
import uk.gov.dvla.vehicles.presentation.common.services.DateService
import uk.gov.dvla.vehicles.presentation.common.webserviceclients.addresslookup.{AddressLookupService, AddressLookupWebService}
import uk.gov.dvla.vehicles.presentation.common.webserviceclients.healthstats.{HealthStatsFailure, HealthStatsSuccess, HealthStats}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


object AddressLookupServiceImpl {
  final val ServiceName = "os-address-lookup-microservice"
}

final class AddressLookupServiceImpl @Inject()(ws: AddressLookupWebService,
                                               dateService: DateService,
                                               healthStats: HealthStats) extends AddressLookupService with DVLALogger {
  import AddressLookupServiceImpl.ServiceName

  override def fetchAddressesForPostcode(postcode: String, trackingId: TrackingId, showBusinessName: Option[Boolean] = None)
                                        (implicit lang: Lang): Future[Seq[(String, String)]] = {

    def extractFromJson(resp: WSResponse): Option[PostcodeToAddressResponseDto] =
      resp.json.asOpt[PostcodeToAddressResponseDto]

    def toDropDown(resp: WSResponse): Seq[(String, String)] =
      extractFromJson(resp) match {
        case Some(results) =>
          results.addresses.map(address => (address.uprn, address.address))
        case None =>
          // Handle no results
          val postcodeToLog = LogFormats.anonymize(postcode)
          logMessage(trackingId, Debug, s"No results returned for postcode: $postcodeToLog")
          Seq.empty// Exception case and empty seq case are treated the same in the UI
      }

    ws.callPostcodeWebService(postcode, trackingId, showBusinessName)(lang).map { resp =>
      logMessage(trackingId, Debug,s"Http response code from Ordnance Survey postcode lookup " +
        s"service was: ${resp.status}")
      if (resp.status == play.api.http.Status.OK) {
        healthStats.success(HealthStatsSuccess(ServiceName, dateService.now))
        toDropDown(resp)
      }
      else {
        logMessage(trackingId, Error, s"Post code service returned abnormally " +
          s"'${resp.status}: ${resp.body}'")
        healthStats.failure(HealthStatsFailure(ServiceName, dateService.now, new Exception()))
        Seq.empty // The service returned http code other than 200 OK
      }
    }.recover {
      case e: Throwable =>
        logMessage(trackingId, Error, s"Ordnance Survey postcode lookup service error.$e")
        healthStats.failure(HealthStatsFailure(ServiceName, dateService.now, e))
        Seq.empty // Exception case and empty seq case are treated the same in the UI
    }
  }

  override def fetchAddressForUprn(uprn: String, trackingId: TrackingId)
                                  (implicit lang: Lang): Future[Option[AddressModel]] = {

    // Extract result from response and return as a view model.
    def extractFromJson(resp: WSResponse): Option[UprnToAddressResponseDto] = {
      resp.json.asOpt[UprnToAddressResponseDto]
    }

    def toViewModel(resp: WSResponse) =
      extractFromJson(resp) match {
        case Some(deserialized) => deserialized.addressViewModel
        case None =>
          val uprnToLog = LogFormats.anonymize(uprn)
          logMessage(trackingId, Error,s"Could not deserialize response of web service for " +
            s"submitted UPRN: $uprnToLog")
          None
      }

    ws.callUprnWebService(uprn, trackingId).map { resp =>
      logMessage(trackingId, Debug,s"Http response code from Ordnance Survey uprn lookup " +
        s"service was: ${resp.status}")
      if (resp.status == play.api.http.Status.OK) {
        healthStats.success(HealthStatsSuccess(ServiceName, dateService.now))
        toViewModel(resp)
      } else {
        logMessage(trackingId, Error,s"Post code service returned abnormally " +
          s"'${resp.status}: ${resp.body}'")
        healthStats.failure(HealthStatsFailure(ServiceName, dateService.now, new Exception()))
        None
      }
    }.recover {
      case e: Throwable =>
        logMessage(trackingId, Error, s"Ordnance Survey postcode lookup service error $e")
        healthStats.failure(HealthStatsFailure(ServiceName, dateService.now, e))
        None
    }
  }

  def addresses(postcode: String, trackingId: TrackingId)
               (implicit lang: Lang): Future[Seq[AddressDto]] = {
    ws.callAddresses(postcode, trackingId)(lang).map { resp =>
      logMessage(trackingId, Debug,s"Http response code from Ordnance Survey postcode lookup " +
        s"service was: ${resp.status}")
      if (resp.status == play.api.http.Status.OK) {
        healthStats.success(HealthStatsSuccess(ServiceName, dateService.now))
        try resp.json.as[Seq[AddressDto]]
        catch {
          case e: Throwable =>
            logMessage(trackingId, Error, s"Ordnance Survey postcode lookup service error: $e")
            Seq.empty //  return empty seq given invalid json
        }
      }
      else {
        logMessage(trackingId, Error, s"Post code service returned abnormally " +
          s"'${resp.status}: ${resp.body}'")
        healthStats.failure(HealthStatsFailure(ServiceName, dateService.now, new Exception()))
        Seq.empty // The service returned http code other than 200 OK
      }
    }.recover {
      case e: Throwable =>
        logMessage(trackingId, Error,s"Ordnance Survey postcode lookup service error: $e")
        healthStats.failure(HealthStatsFailure(ServiceName, dateService.now, e))
        Seq.empty // Exception case and empty seq case are treated the same in the UI
    }
  }
}
