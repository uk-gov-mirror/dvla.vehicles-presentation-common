package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.DisposePage


class DisposeSuccessControllerSpec extends WordSpec with Matchers with Mockito {

  "Disposal success controller" should {

    "present" in new WithApplication {
      // Arrange
      CacheSetup.setupTradeDetails()
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()
      CacheSetup.disposeFormModel()
      CacheSetup.disposeTransactionId()
      CacheSetup.vehicleRegistrationNumber()
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeSuccess.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to next page after the new disposal button is clicked" in new WithApplication {
      CacheSetup.setupTradeDetails()
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()
      CacheSetup.disposeFormModel()
      CacheSetup.disposeTransactionId()
      CacheSetup.vehicleRegistrationNumber()
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeSuccess.submit(request)

      // Assert
      redirectLocation(result) should equal(Some(VehicleLookupPage.urlControllerTest))
    }

    "redirect to SetUpTradeDetails on present when cache is empty" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeSuccess.present(request)

      // Assert
      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.urlControllerTest))
    }

    "redirect to SetUpTradeDetails on present when only DealerDetails are cached" in new WithApplication {
      // Arrange
      CacheSetup.businessChooseYourAddress()
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeSuccess.present(request)

      // Assert
      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.urlControllerTest))
    }

    "redirect to SetUpTradeDetails on present when only VehicleDetails are cached" in new WithApplication {
      // Arrange
      CacheSetup.vehicleDetailsModel()
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeSuccess.present(request)

      // Assert
      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.urlControllerTest))
    }

    "redirect to SetUpTradeDetails on present when only DisposeDetails are cached" in new WithApplication {
      // Arrange
      CacheSetup.disposeModel()
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeSuccess.present(request)

      // Assert
      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.urlControllerTest))
    }

    "redirect to SetUpTradeDetails on present when only VehicleDetails and DisposeDetails are cached" in new WithApplication {
      // Arrange
      CacheSetup.vehicleDetailsModel()
      DisposePage.setupDisposeFormModelCache()

      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeSuccess.present(request)

      // Assert
      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.urlControllerTest))
    }

    "redirect to SetUpTradeDetails on present when only VehicleDetails and DealerDetails are cached" in new WithApplication {
      // Arrange
      CacheSetup.vehicleDetailsModel()
      CacheSetup.businessChooseYourAddress()
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeSuccess.present(request)

      // Assert
      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.urlControllerTest))
    }

    "redirect to SetUpTradeDetails on present when only DisposeDetails and DealerDetails are cached" in new WithApplication {
      // Arrange
      CacheSetup.businessChooseYourAddress()
      CacheSetup.disposeModel()
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeSuccess.present(request)

      // Assert
      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.urlControllerTest))
    }

    "redirect to SetUpTradeDetails on submit when cache is empty" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeSuccess.submit(request)

      // Assert
      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.urlControllerTest))
    }

    "redirect to SetUpTradeDetails on submit when only DealerDetails are cached" in new WithApplication {
      // Arrange
      CacheSetup.businessChooseYourAddress()
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeSuccess.submit(request)

      // Assert
      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.urlControllerTest))
    }

    "redirect to SetUpTradeDetails on submit when only VehicleDetails are cached" in new WithApplication {
      // Arrange
      CacheSetup.vehicleDetailsModel()
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeSuccess.submit(request)

      // Assert
      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.urlControllerTest))
    }

    "redirect to SetUpTradeDetails on submit when only DisposeDetails are cached" in new WithApplication {
      // Arrange
      CacheSetup.disposeModel()
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeSuccess.submit(request)

      // Assert
      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.urlControllerTest))
    }

    "redirect to SetUpTradeDetails on submit when only VehicleDetails and DisposeDetails are cached" in new WithApplication {
      // Arrange
      CacheSetup.vehicleDetailsModel()
      DisposePage.setupDisposeFormModelCache()

      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeSuccess.submit(request)

      // Assert
      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.urlControllerTest))
    }

    "redirect to SetUpTradeDetails on submit when only VehicleDetails and DealerDetails are cached" in new WithApplication {
      // Arrange
      CacheSetup.vehicleDetailsModel()
      CacheSetup.businessChooseYourAddress()
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeSuccess.submit(request)

      // Assert
      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.urlControllerTest))
    }

    "redirect to SetUpTradeDetails on submit when only DisposeDetails and DealerDetails are cached" in new WithApplication {
      // Arrange
      CacheSetup.businessChooseYourAddress()
      CacheSetup.disposeModel()
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.DisposeSuccess.submit(request)

      // Assert
      redirectLocation(result) should equal(Some(SetupTradeDetailsPage.urlControllerTest))
    }

  }
}