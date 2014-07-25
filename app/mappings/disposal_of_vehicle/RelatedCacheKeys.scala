package mappings.disposal_of_vehicle

import mappings.common.Help.HelpCacheKey

import models.domain.disposal_of_vehicle.BusinessChooseYourAddressModel.BusinessChooseYourAddressCacheKey
import models.domain.disposal_of_vehicle.DisposeFormModel.DisposeFormModelCacheKey
import models.domain.disposal_of_vehicle.DisposeFormModel.DisposeFormRegistrationNumberCacheKey
import models.domain.disposal_of_vehicle.DisposeFormModel.DisposeFormTimestampIdCacheKey
import models.domain.disposal_of_vehicle.DisposeFormModel.DisposeFormTransactionIdCacheKey
import models.domain.disposal_of_vehicle.DisposeFormModel.DisposeOccurredCacheKey
import models.domain.disposal_of_vehicle.DisposeFormModel.PreventGoingToDisposePageCacheKey
import models.domain.disposal_of_vehicle.DisposeModel.DisposeModelCacheKey
import viewmodels.{BruteForcePreventionViewModel, EnterAddressManuallyViewModel}
import EnterAddressManuallyViewModel.EnterAddressManuallyCacheKey
import mappings.disposal_of_vehicle.MicroserviceError.MicroServiceErrorRefererCacheKey
import models.domain.disposal_of_vehicle.SetupTradeDetailsModel.SetupTradeDetailsCacheKey
import BruteForcePreventionViewModel.BruteForcePreventionViewModelCacheKey
import models.domain.disposal_of_vehicle.TraderDetailsModel.TraderDetailsCacheKey
import models.domain.disposal_of_vehicle.VehicleDetailsModel.VehicleLookupDetailsCacheKey
import models.domain.disposal_of_vehicle.VehicleLookupFormModel.VehicleLookupFormModelCacheKey
import models.domain.disposal_of_vehicle.VehicleLookupFormModel.VehicleLookupResponseCodeCacheKey

object RelatedCacheKeys {
  final val SeenCookieMessageKey = "seen_cookie_message"

  // TODO: what is this set of cookies for?
  val DisposeOnlySet = Set(
    DisposeFormModelCacheKey,
    DisposeFormTransactionIdCacheKey,
    DisposeFormTimestampIdCacheKey,
    DisposeFormRegistrationNumberCacheKey,
    DisposeModelCacheKey
  )

  // Set of cookies related to a single vehicle disposal. Removed once the vehicle is successfully disposed
  val DisposeSet = Set(
    BruteForcePreventionViewModelCacheKey,
    VehicleLookupDetailsCacheKey,
    VehicleLookupResponseCodeCacheKey,
    VehicleLookupFormModelCacheKey,
    DisposeFormModelCacheKey,
    DisposeFormTransactionIdCacheKey,
    DisposeFormTimestampIdCacheKey,
    DisposeFormRegistrationNumberCacheKey,
    DisposeModelCacheKey
  )

  // Set of cookies that store the trade details data. These are retained after a successful disposal
  // so the trader does not have to re-enter their details when disposing subsequent vehicles
  val TradeDetailsSet = Set(SetupTradeDetailsCacheKey,
      TraderDetailsCacheKey,
      BusinessChooseYourAddressCacheKey,
      EnterAddressManuallyCacheKey)

  // The full set of cache keys. These are removed at the start of the process in the "before_you_start" page
  val FullSet = TradeDetailsSet.++(DisposeSet)
                              .++(Set(PreventGoingToDisposePageCacheKey))
                              .++(Set(DisposeOccurredCacheKey))
                              .++(Set(HelpCacheKey))
                              .++(Set(MicroServiceErrorRefererCacheKey))
}
