package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.WebDriverFactory
import helpers.webbrowser._

object BeforeYouStartPage extends Page with WebBrowser {

  override val url: String = WebDriverFactory.baseUrl
  override val title: String = "Dispose a vehicle into the motor trade: set-up"

  def startNow(implicit driver: WebDriver): Element = find(id("next")).get
}