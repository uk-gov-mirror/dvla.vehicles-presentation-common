package uk.gov.dvla.vehicles.presentation.common.pages

import java.util.Calendar

import uk.gov.dvla.vehicles.presentation.common.helpers
import uk.gov.dvla.vehicles.presentation.common.models
import helpers.webbrowser.{Page, WebDriverFactory}
import models.DateOfSaleModel.Form.{OptionalDateId, DateId}
import org.openqa.selenium.WebDriver
import org.scalatest.selenium.WebBrowser._

class DateOfSalePage(implicit driver: WebDriver) extends Page {

  final val address = "/date-of-sale"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  final override val title: String = "Date Of Sale Input"

  lazy val optional: DateOfSaleWidget = DateOfSaleWidget(OptionalDateId)

  lazy val required: DateOfSaleWidget = DateOfSaleWidget(DateId)

  def submit(implicit driver: WebDriver): Element = find(id("submit")).get

  // NOTE: use valid (arbitrary) default values - Q. why does optional have defaults?
  def navigate(day: String = "01", month: String = "01", year: String = Calendar.getInstance().get(Calendar.YEAR).toString,
               day1: String = "01", month1: String = "01", year1: String = Calendar.getInstance().get(Calendar.YEAR).toString)
              (implicit driver: WebDriver) {
    go to this

    optional.day.value = day
    optional.month.value = month
    optional.year.value = year

    required.day.value = day1
    required.month.value = month1
    required.year.value = year1

    click on submit
  }


}

object DateOfSalePage {
  def instance(implicit driver: WebDriver) = new DateOfSalePage


  def useTodaysDateButton(implicit driver: WebDriver): Element = {
    // This is the element / dom node that must be clicked to show / hide content
    find(cssSelector("#todays_date")) getOrElse(throw new Exception("Unable to find help icon "))
  }
}


class DateOfSaleWidget(idStr: String)(implicit driver: WebDriver) {
  def label: String = ???

  def hint: String = ???

  lazy val day: TelField = telField(id(s"${idStr}_day"))

  lazy val month: TelField = telField(id(s"${idStr}_month"))

  lazy val year: TelField = telField(id(s"${idStr}_year"))

}

object DateOfSaleWidget {
  def apply(idStr: String)(implicit driver: WebDriver): DateOfSaleWidget = new DateOfSaleWidget(idStr)
}
