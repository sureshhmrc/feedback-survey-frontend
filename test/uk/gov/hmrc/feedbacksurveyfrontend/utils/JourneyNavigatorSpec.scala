/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.feedbacksurveyfrontend.utils

import uk.gov.hmrc.feedbacksurveyfrontend.utils.JourneyNavigator._
import utils.UnitTestTraits


class JourneyNavigatorSpec extends UnitTestTraits {
  "The journey navigator" should {
    "return the correct next page for ableToDoPage" in {
      JourneyNavigator.nextPage("", ableToDoPage) shouldBe usingServicePage
    }
    "return the correct next page for usingServicePage" in {
      JourneyNavigator.nextPage("", usingServicePage) shouldBe aboutServicePage
    }
    "return the correct next page for aboutServicePage" in {
      JourneyNavigator.nextPage("", aboutServicePage) shouldBe recommendServicePage
    }
    "return the correct next page for aboutServicePage for PODS" in {
      JourneyNavigator.nextPage("PODS", aboutServicePage) shouldBe thankyouPage
    }
    "return the correct next page for recommendServicePage" in {
      JourneyNavigator.nextPage("", recommendServicePage) shouldBe thankyouPage
    }
  }
}
