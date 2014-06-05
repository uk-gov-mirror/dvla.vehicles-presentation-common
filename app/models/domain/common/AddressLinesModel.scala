package models.domain.common

import mappings.common.AddressLines._
import play.api.libs.json.Json

case class AddressLinesModel(buildingNameOrNumber: String,
                             line2: Option[String] = None,
                             line3: Option[String] = None,
                             postTown: String) {
  def toViewFormat: Seq[String] = Seq(Some(buildingNameOrNumber), line2, line3, Some(postTown)).flatten

  def totalCharacters = toViewFormat.map(_.length).sum
}

object AddressLinesModel {
  implicit val JsonFormat = Json.format[AddressLinesModel]
  implicit val Key = CacheKey[AddressLinesModel](AddressLinesCacheKey)
}