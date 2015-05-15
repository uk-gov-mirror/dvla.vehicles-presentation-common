package uk.gov.dvla.vehicles.presentation.common.views.widgetdriver

import org.openqa.selenium.support.ui.{Select, ExpectedCondition, ExpectedConditions, WebDriverWait}
import org.openqa.selenium.{By, WebDriver}
import uk.gov.dvla.vehicles.presentation.common.views.widgetdriver.Wait.{elementHasAnyText}
import uk.gov.dvla.vehicles.presentation.common.helpers.webbrowser.{SingleSel, Element, WebBrowserDSL, TextField, Checkbox}
import org.openqa.selenium.support.ui.ExpectedConditions.{elementToBeSelected, invisibilityOfElementLocated}
import uk.gov.dvla.vehicles.presentation.common
import common.mappings.AddressPicker.{AddressLine1Id, AddressLine2Id, AddressLine3Id, PostTownId, PostcodeId, RememberId}

class AddressPickerDriver(id: String)  extends WebBrowserDSL {

  def postCodeSearch(implicit driver: WebDriver): TextField =
    textField(id("address-postcode-lookup"))(driver.findElement(By.id(id)))

  def searchButton(implicit driver: WebDriver): Element =
    find(id("address-find"))(driver.findElement(By.id(id)))
      .getOrElse(throw new Exception(s"Cannot find element with id address-postcode-lookup in address picker with id:$id "))

  def enterManuallyLink(implicit driver: WebDriver): Element =
    find(cssSelector(".address-manual-toggle"))(driver.findElement(By.id(id)))
      .getOrElse(throw new Exception(s"Cannot find element with id address-manual-toggle in address picker with id:$id "))

  def search(postcode: String)(implicit driver: WebDriver): Unit = {
    postCodeSearch.value = postcode
    click on searchButton
    Wait.until(selectPopulated)
  }

  def manualEnter()(implicit driver: WebDriver): Unit = {
    click on enterManuallyLink
    Wait.until(invisibilityOfElementLocated(By.cssSelector(s"#$id .postcode-lookup-container")))
  }

  def addressSelect(implicit driver: WebDriver): SingleSel =
    singleSel(id("address-list"))(driver.findElement(By.id(id)))

  def selectAddress(value: String)(implicit driver: WebDriver): Unit = {
    addressSelect.value = value
//    Wait.until(elementHasAnyText(By.cssSelector(s"#$id #${id}_addressLines-1")))
  }

  def addressLine1(implicit driver: WebDriver): TextField =
    textField(id(s"${id}_${AddressLine1Id}"))(driver.findElement(By.id(id)))

  def addressLine2(implicit driver: WebDriver): TextField =
    textField(id(s"${id}_${AddressLine2Id}"))(driver.findElement(By.id(id)))

  def addressLine3(implicit driver: WebDriver): TextField =
    textField(id(s"${id}_${AddressLine3Id}"))(driver.findElement(By.id(id)))

  def town(implicit driver: WebDriver): TextField =
    textField(id(s"${id}_${PostTownId}"))(driver.findElement(By.id(id)))

  def postcode(implicit driver: WebDriver): TextField =
    textField(id(s"${id}_${PostcodeId}"))(driver.findElement(By.id(id)))

  def remember(implicit driver: WebDriver): Checkbox =
    checkbox(id(s"${id}_${RememberId}"))(driver.findElement(By.id(id)))

  def assertAddressInputsVisible(implicit driver: WebDriver): Unit = {
    Wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(s"#$id .address-manual-inputs-wrapper")))
  }

  def assertAddressInputsInvisible(implicit driver: WebDriver): Unit = {
    Wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(s"#$id .address-manual-inputs-wrapper")))
  }

  def assertAddressListVisible(implicit driver: WebDriver): Unit = {
    Wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(s"#$id .address-list-wrapper")))
  }

  def assertAddressListInvisible(implicit driver: WebDriver): Unit = {
    Wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(s"#$id .address-list-wrapper")))
  }

  def assertLookupInputVisible(implicit driver: WebDriver): Unit = {
    Wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(s"#$id .postcode-lookup-container")))
  }

  def assertServerErrorVisible(implicit driver: WebDriver): Unit = {
    Wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(s"#$id .server-message")))
  }

  def assertServerErrorInvisible(implicit driver: WebDriver): Unit = {
    Wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(s"#$id .server-message")))
  }

  def assertMissingPostcodeVisible(implicit driver: WebDriver): Unit = {
    Wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(s"#$id .missing-postcode")))
  }

  def assertMissingPostcodeInvisible(implicit driver: WebDriver): Unit = {
    Wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(s"#$id .missing-postcode")))
  }

  def assertLookupInputInvisible(implicit driver: WebDriver): Unit = {
    Wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(s"#$id .postcode-lookup-container")))
  }

  def enterAddressManuallyLink(implicit driver: WebDriver): Element =
    find(cssSelector(s"#$id .address-manual-toggle")) getOrElse(
      throw new Exception(s"Unable to find element with id css selector (#$id .address-manual-toggle)")
    )

  def changeMyDetailsLink(implicit driver: WebDriver): Element =
    find(cssSelector(s"#$id .address-reset-form")) getOrElse(
      throw new Exception(s"Unable to find element with css selector (#$id .address-reset-form)")
    )


  private def selectPopulated: ExpectedCondition[Boolean] = {
    new ExpectedCondition[Boolean]() {
      override def apply(driver: WebDriver): Boolean = {
        println("data-ajax:" + driver.findElement(By.id("address-list")).getAttribute("data-ajax"))
        try driver.findElement(By.id("address-list")).getAttribute("data-ajax") == "true"
        catch {
          case e: Throwable =>
            println(e)
            false
        }
      }
    }
  }
}
