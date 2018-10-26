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

package uk.gov.hmrc.feedbacksurveyfrontend.services

import uk.gov.hmrc.feedbacksurveyfrontend.services
import uk.gov.hmrc.play.binders.Origin
import utils.UnitTestTraits


class OriginServiceSpec extends UnitTestTraits {

  val originService = new OriginService {
    override lazy val originConfigItems = List(
      OriginConfigItem(Some("TOKEN1"), None, None),
      OriginConfigItem(Some("TOKEN2"), Some("http://example.com/custom-feedback-url"), None),
      OriginConfigItem(Some("TOKEN3"), None, "BTA")
    )
  }

  "The validation of an origin" should {

    "pass with valid origin token" in {

      originService.isValid(Origin("TOKEN1")) shouldBe true
      originService.isValid(Origin("TOKEN2")) shouldBe true
    }

    "fail with an invalid origin" in {

      originService.isValid(Origin("INVALIDORIGIN")) shouldBe false
    }
  }

  "The customFeedbackUrl of an origin" should {

    "return a custom feedback url if present" in {

      originService.customFeedbackUrl(Origin("TOKEN2")) shouldBe Some("http://example.com/custom-feedback-url")
    }

    "not return a custom feedback url if not present" in {

      originService.customFeedbackUrl(Origin("TOKEN1")) shouldBe None
    }
  }

  "The taxAccount of an origin" should {

    "return a tax account if present" in {

      originService.taxAccount(Origin("TOKEN3")) shouldBe Some("BTA")
    }

    "not return a tax account if not present" in {

      originService.taxAccount(Origin("TOKEN1")) shouldBe None
    }
  }
}
