package uk.gov.dvla.vehicles.presentation.common.views

import com.github.nscala_time.time.Imports.LocalDate
import org.joda.time.chrono.ISOChronology

import java.util.Calendar

import org.scalatest.selenium.WebBrowser._
import play.api.i18n.Messages
import uk.gov.dvla.vehicles.presentation.common.composition.TestHarness
import uk.gov.dvla.vehicles.presentation.common.helpers.UiSpec
import uk.gov.dvla.vehicles.presentation.common.pages.{DateOfSalePage, ErrorPanel}

class DateOfSaleIntegrationSpec extends UiSpec with TestHarness {

  val validDos = Calendar.getInstance()
  validDos.add(Calendar.YEAR, -1)

  "DateOfSale" should {
    "be on a page with the correct title" in new WebBrowserForSelenium {
      go to DateOfSalePage.instance
      pageTitle should equal(DateOfSalePage.instance.title)
    }

    "allow no values to be input" in new WebBrowserForSelenium {
      DateOfSalePage.instance.navigate("", "", "") // required date contain defaults
      pageTitle should equal("Success")
      ErrorPanel.hasErrors should equal(false)
      ErrorPanel.numberOfErrors should equal(0)
    }

    "validate partial input" in new WebBrowserForSelenium {
      DateOfSalePage.instance.navigate("", "", validDos.get(Calendar.YEAR).toString)
      ErrorPanel.text should include(Messages("error.date.invalid"))
      ErrorPanel.numberOfErrors should equal(1)
    }

    "validate the day if there is any input" in new WebBrowserForSelenium {
      val month = f"${validDos.get(Calendar.MONTH)}%02d"
      DateOfSalePage.instance.navigate("oij", month, validDos.get(Calendar.YEAR).toString)
      ErrorPanel.text should include(Messages("error.date.invalid"))
      ErrorPanel.numberOfErrors should equal(1)
    }

    "validate the whole date with invalid day" in new WebBrowserForSelenium {
      DateOfSalePage.instance.navigate("31", "09", validDos.get(Calendar.YEAR).toString)
      ErrorPanel.text should include(Messages("error.date.invalid"))
      ErrorPanel.numberOfErrors should equal(1)
    }

    "validate the month if there is any input" in new WebBrowserForSelenium {
      val date = f"${validDos.get(Calendar.DATE)}%02d"
      DateOfSalePage.instance.navigate(date, "we", validDos.get(Calendar.YEAR).toString)
      ErrorPanel.text should include(Messages("error.date.invalid"))
      ErrorPanel.numberOfErrors should equal(1)
    }

    "validate the year if there is any input" in new WebBrowserForSelenium {
      val today = Calendar.getInstance()
      val date = f"${today.get(Calendar.DATE)}%02d"
      val month = f"${today.get(Calendar.MONTH)}%02d"
      DateOfSalePage.instance.navigate(date, month, "wwer")
      ErrorPanel.text should include(Messages("error.date.invalid"))
      ErrorPanel.numberOfErrors should equal(1)
    }

    "validate the whole date is not in the future" in new WebBrowserForSelenium {
      val chronology = ISOChronology.getInstance()
      val now = System.currentTimeMillis()
      val day = chronology.dayOfMonth().get(now)
      val month = chronology.monthOfYear().get(now)
      val year = chronology.year().get(now)

      DateOfSalePage.instance.navigate((day + 1).toString, month.toString, year.toString)

      ErrorPanel.numberOfErrors should equal(1)
    }

    "Pass through valid dates" in new WebBrowserForSelenium {
      def success(day: String, month: String, year: String): Unit = {
        DateOfSalePage.instance.navigate(day.toString, month.toString, year.toString)
        pageTitle should equal("Success")
      }

      success("01", "02", validDos.get(Calendar.YEAR).toString)
      success("31", "12", validDos.get(Calendar.YEAR).toString)
      val today = LocalDate.today
      success(today.toString("dd"), today.toString("MM"), today.toString("YYYY"))
    }

    "Not allow any empty fields in required date" in new WebBrowserForSelenium {
      DateOfSalePage.instance.navigate("01", "01", validDos.get(Calendar.YEAR).toString, "", "", "")
      ErrorPanel.text should include(Messages("error.date.invalid"))
      ErrorPanel.numberOfErrors should equal(1)
    }

    "set today's date if the button is clicked" in new WebBrowserWithJs {
      go to DateOfSalePage.instance

      val today = LocalDate.today

      DateOfSalePage.instance.waitUntilJavascriptReady
      click on DateOfSalePage.useTodaysDateButton

      DateOfSalePage.instance.required.day.value should equal(today.toString("dd"))
      DateOfSalePage.instance.required.month.value should equal(today.toString("MM"))
      DateOfSalePage.instance.required.year.value should equal(today.toString("YYYY"))
    }

    "only allow numbers to be typed in" in new WebBrowserWithJs {
      val alphaChars = "#$%&'()*+,-./:;<=>?@[\\]^{|}~ \"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
      val today = LocalDate.today
      go to DateOfSalePage.instance
      DateOfSalePage.instance.waitUntilJavascriptReady

      DateOfSalePage.instance.required.day.underlying.sendKeys(alphaChars.concat(today.toString("dd")))
      DateOfSalePage.instance.required.month.underlying.sendKeys(alphaChars.concat(today.toString("MM")))
      DateOfSalePage.instance.required.year.underlying.sendKeys(alphaChars.concat(today.toString("YYYY")))

      DateOfSalePage.instance.required.day.value should equal(today.toString("dd"))
      DateOfSalePage.instance.required.month.value should equal(today.toString("MM"))
      DateOfSalePage.instance.required.year.value should equal(today.toString("YYYY"))
    }
  }
}
