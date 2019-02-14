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

package uk.gov.hmrc.feedbacksurveyfrontend.bindable

import controllers.bindable.Binders
import uk.gov.hmrc.play.binders.ContinueUrl
import utils.UnitTestTraits

class BindableSpec extends UnitTestTraits {

  "Calling continueUrlBinder.unbind" should {

    "return the key and the ContinueUrl" in {

      Binders.continueUrlBinder.unbind("continue", ContinueUrl("/relative/url")) shouldBe "continue=%2Frelative%2Furl"

    }
  }

  "Calling continueUrlBinder.bind" should {

    "return an url when called with a relative url" in {

      val url = "/relative/url"
      Binders.continueUrlBinder.bind("continue", Map("continue" -> Seq(url))) shouldBe Some(Right(ContinueUrl(url)))
    }

    "return error when not url" in {

      val url = "gtuygyg"
      Binders.continueUrlBinder.bind("continue", Map("continue" -> Seq(url))) shouldBe Some(Left(s"'$url' is not a valid continue URL"))
    }

    "return error for urls with /\\" in {

      val url = "/\\www.example.com"
      Binders.continueUrlBinder.bind("continue", Map("continue" -> Seq(url))) shouldBe Some(Left(s"'$url' is not a valid continue URL"))
    }

    "return error for none relative urls" in {

      val url = "http://nonrelativeurl.com"
      Binders.continueUrlBinder.bind("continue", Map("continue" -> Seq(url))) shouldBe Some(Left(s"'$url' is not a valid continue URL"))
    }
  }
}
