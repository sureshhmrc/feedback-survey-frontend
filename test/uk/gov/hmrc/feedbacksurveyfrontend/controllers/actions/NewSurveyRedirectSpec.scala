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

package controllers.actions

import org.scalatest.BeforeAndAfterEach
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import play.api.mvc.{AnyContent, Request, Result}
import uk.gov.hmrc.feedbacksurveyfrontend.AppConfig
import uk.gov.hmrc.play.test.UnitSpec
import play.api.mvc.Results._
import play.api.test.FakeRequest
import play.api.test.Helpers.redirectLocation
import uk.gov.hmrc.play.binders.Origin
import play.api.test.Helpers._

class NewSurveyRedirectSpec extends UnitSpec with MockitoSugar with BeforeAndAfterEach {

  val mockAppConfig = mock[AppConfig]
  val fakeRequest = FakeRequest("GET", "")

  val redirect: String = "test-url"
  val f: Request[AnyContent] => Result = _ => Redirect(redirect)

  val action = new NewSurveyRedirect {
    override val appConfig: AppConfig = mockAppConfig
  }

  override def beforeEach(): Unit = {
    reset(mockAppConfig)
    when(mockAppConfig.redirectToNewSurveyEnabled).thenReturn(true)
    when(mockAppConfig.newFeedbackUrl).thenReturn("feedback")
  }

  "newSurveyRedirect" should {
    "redirect to feedback" in {
      val result = action.apply("test")(f).apply(fakeRequest)
      redirectLocation(result) shouldBe Some("feedback/test")
    }

    "redirect to personal feedback" in {
      val result = action.apply("PERTAX")(f).apply(fakeRequest)
      redirectLocation(result) shouldBe Some("feedback/PERTAX/personal")
    }

    "return result of function parameter" in {
      when(mockAppConfig.redirectToNewSurveyEnabled).thenReturn(false)

      val result = action.apply("test")(f).apply(fakeRequest)
      redirectLocation(result) shouldBe Some(redirect)
    }
  }
}