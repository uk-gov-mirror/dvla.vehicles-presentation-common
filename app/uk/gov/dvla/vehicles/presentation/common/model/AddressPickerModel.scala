package uk.gov.dvla.vehicles.presentation.common.model

case class AddressPickerModel(streetAddress1: String,
                              streetAddress2: Option[String],
                              streetAddress3: Option[String],
                              postTown: String,
                              county: Option[String],
                              postCode: String)
