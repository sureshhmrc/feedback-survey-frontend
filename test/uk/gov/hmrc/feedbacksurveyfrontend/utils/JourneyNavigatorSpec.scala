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

  private val journeySkipItems1 = List( ("aboutServicePage","thankyouPage"))
  private val journeySkipItems2 = List.empty
  private val journeySkipItems3 = List( ("aboutServicePage","bla"))

  "The journey navigator" should {
    "return the correct next page for ableToDoPage" in {
      JourneyNavigator.nextPage(journeySkipItems2, ableToDoPage) shouldBe usingServicePage
    }
    "return the correct next page for usingServicePage" in {
      JourneyNavigator.nextPage(journeySkipItems2, usingServicePage) shouldBe aboutServicePage
    }
    "return the correct next page for aboutServicePage" in {
      JourneyNavigator.nextPage(journeySkipItems2, aboutServicePage) shouldBe recommendServicePage
    }
    "return the correct next page when default page is skipped" in {
      JourneyNavigator.nextPage(journeySkipItems1, aboutServicePage) shouldBe thankyouPage
    }
    "return the correct next page when default page is skipped but the skip destination is not recognized" in {
      JourneyNavigator.nextPage(journeySkipItems3, aboutServicePage) shouldBe recommendServicePage
    }
    "return the correct next page for recommendServicePage" in {
      JourneyNavigator.nextPage(journeySkipItems2, recommendServicePage) shouldBe thankyouPage
    }
  }
}
