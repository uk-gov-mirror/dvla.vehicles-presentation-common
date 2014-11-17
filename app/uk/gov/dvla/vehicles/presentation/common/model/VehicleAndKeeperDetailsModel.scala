package uk.gov.dvla.vehicles.presentation.common.model

import play.api.libs.json.Json
import uk.gov.dvla.vehicles.presentation.common.clientsidesession.CacheKey
import uk.gov.dvla.vehicles.presentation.common.webserviceclients.vehicleandkeeperlookup.VehicleAndKeeperDetailsDto
import uk.gov.dvla.vehicles.presentation.common.views.constraints.Postcode._
import uk.gov.dvla.vehicles.presentation.common.views.constraints.RegistrationNumber._
import uk.gov.dvla.vehicles.presentation.common.views.models.{AddressAndPostcodeViewModel, AddressLinesViewModel}

final case class VehicleAndKeeperDetailsModel(registrationNumber: String,
                                              make: Option[String],
                                              model: Option[String],
                                              title: Option[String],
                                              firstName: Option[String],
                                              lastName: Option[String],
                                              address: Option[AddressModel])

object VehicleAndKeeperDetailsModel {

  // Create a VehicleAndKeeperDetailsDto from the given replacementVRM. We do this in order get the data out of the response from micro-service call
  def from(vehicleAndKeeperDetailsDto: VehicleAndKeeperDetailsDto) = {

    val addressViewModel = {
      val addressLineModel = AddressLinesViewModel(
        vehicleAndKeeperDetailsDto.keeperAddressLine1.get,
        vehicleAndKeeperDetailsDto.keeperAddressLine2,
        vehicleAndKeeperDetailsDto.keeperAddressLine3,
        vehicleAndKeeperDetailsDto.keeperAddressLine4,
        vehicleAndKeeperDetailsDto.keeperPostTown.get
      )
      val addressAndPostcodeModel = AddressAndPostcodeViewModel(None, addressLineModel)
      AddressModel.from(addressAndPostcodeModel, formatPostcode(vehicleAndKeeperDetailsDto.keeperPostcode.get))
    }

    VehicleAndKeeperDetailsModel(registrationNumber = formatVrm(vehicleAndKeeperDetailsDto.registrationNumber),
      make = vehicleAndKeeperDetailsDto.vehicleMake,
      model = vehicleAndKeeperDetailsDto.vehicleModel,
      title = vehicleAndKeeperDetailsDto.keeperTitle,
      firstName = vehicleAndKeeperDetailsDto.keeperFirstName,
      lastName = vehicleAndKeeperDetailsDto.keeperLastName,
      address = Some(addressViewModel))
  }

  implicit val JsonFormat = Json.format[VehicleAndKeeperDetailsModel]
  // TODO : put this cache key definition somewhere sensible
  final val VehicleAndKeeperLookupDetailsCacheKey = "vehicle-and-keeper-lookup-details"
  implicit val Key = CacheKey[VehicleAndKeeperDetailsModel](VehicleAndKeeperLookupDetailsCacheKey)
}