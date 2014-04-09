package helpers.disposal_of_vehicle

import models.domain.disposal_of_vehicle.AddressViewModel
import services.fakes.FakeWebServiceImpl.traderUprnValid
import services.fakes.FakeVehicleLookupWebService._

object Helper {
  val traderBusinessNameValid = "example trader name"
  val traderaddressValid = "1"

  val line1Valid = "123"
  val line2Valid = "line-2 stub"
  val line3Valid = "line-3 stub"
  val line4Valid = "line-4 stub"
  val postcodeValid = "CM81QJ"
  val postcodeValidWithSpace = "CM8 1QJ"
  val postcodeNoResults = "SA99 1DD"

  val vehicleLookupKey = referenceNumberValid + "." + registrationNumberValid

  val address1 = AddressViewModel(address= Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
  val addressWithUprn = AddressViewModel(uprn=Some(traderUprnValid),address= Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
  
  val consentValid = "true"
  val mileageValid = "20000"
  val emailValid = "viv.richards@emailprovider.co.uk"
}
