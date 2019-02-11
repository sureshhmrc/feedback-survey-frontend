/*
 * Copyright 2019 HM Revenue & Customs
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

package uk.gov.hmrc.feedbacksurveyfrontend.controllers

import controllers.LanguageController
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.play.binders.ContinueUrl
import utils.UnitTestTraits

class LanguageControllerSpec extends UnitTestTraits {

  object TestLanguageController extends LanguageController

  "Calling LanguageController.enGb" should {
    "change the language to English and return 303" in {
      val r = TestLanguageController.enGb(ContinueUrl("/change-lang"))(FakeRequest("GET", ""))
      cookies(r).get("PLAY_LANG").get.value shouldBe "en"
      status(r) shouldBe 303
    }
  }

  "Calling LanguageController.cyGb" should {
    "change the language to Welsh and return 303" in {
      val r = TestLanguageController.cyGb(ContinueUrl("/change-lang"))(FakeRequest("GET", ""))
      cookies(r).get("PLAY_LANG").get.value shouldBe "cy"
      status(r) shouldBe 303
    }
  }
}
