package uk.gov.dvla.vehicles.presentation.common.webserviceclients.bruteforceprevention

import uk.gov.dvla.vehicles.presentation.common.ConfigProperties.{getProperty, getOptionalProperty}

class BruteForcePreventionConfig {

//  val baseUrl: String = getProperty("bruteForcePrevention.baseUrl", "NOT FOUND")
//  val requestTimeoutMillis: Int = getProperty("bruteForcePrevention.requestTimeout", 5.seconds.toMillis.toInt)
//  val isEnabled: Boolean = getProperty("bruteForcePrevention.enabled", default = true)
//  val nameHeader: String = getProperty("bruteForcePrevention.headers.serviceName", "")
//  val maxAttemptsHeader: Int = getProperty("bruteForcePrevention.headers.maxAttempts", 3)
//  val expiryHeader: String = getProperty("bruteForcePrevention.headers.expiry", "")

  lazy val baseUrl: String = getOptionalProperty[String]("bruteForcePrevention.baseUrl").getOrElse("")
  lazy val requestTimeoutMillis: Int = getOptionalProperty[Int]("bruteForcePrevention.requestTimeout").getOrElse(10000)
  lazy val isEnabled: Boolean = getOptionalProperty[Boolean]("bruteForcePrevention.enabled").getOrElse(false)
  lazy val nameHeader: String = getOptionalProperty[String]("bruteForcePrevention.headers.serviceName").getOrElse("")
  lazy val maxAttemptsHeader: Int = getOptionalProperty[Int]("bruteForcePrevention.headers.maxAttempts").getOrElse(3)
  lazy val expiryHeader: String = getOptionalProperty[String]("bruteForcePrevention.headers.expiry").getOrElse("")
}
