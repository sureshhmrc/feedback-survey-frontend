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

package controllers

import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.feedbacksurveyfrontend.services.{OriginConfigItem, OriginService}
import uk.gov.hmrc.feedbacksurveyfrontend.utils.MockTemplateRenderer
import uk.gov.hmrc.play.binders.Origin
import uk.gov.hmrc.renderer.TemplateRenderer
import utils.UnitTestTraits


class HomeControllerTest extends UnitTestTraits {

  "ableToDo page GET" should {

    def buildFakeHomeController: HomeController = new HomeController{

      override implicit val templateRenderer: TemplateRenderer = MockTemplateRenderer

      val originService: OriginService = new OriginService {
        override lazy val originConfigItems = List(
          OriginConfigItem(Some("TOKEN1"), None, List.empty)
        )
      }
    }

    "give a status of OK, return error page if origin token not found" in {
      val controllerUnderTest = buildFakeHomeController
      val result = controllerUnderTest.start(Origin("TOKEN2")).apply(FakeRequest("GET", ""))
      status(result) shouldBe OK
      contentAsString(result) should include("Service unavailable")
    }

    "give a status of OK, if origin token found" in {
      val controllerUnderTest = buildFakeHomeController
      val result = controllerUnderTest.start(Origin("TOKEN1")).apply(FakeRequest("GET", ""))
      status(result) shouldBe SEE_OTHER
    }

    "redirect to FeedbackSurveyController.ableToDo when origin is valid" in {
      val controllerUnderTest = buildFakeHomeController
      val result = controllerUnderTest.start(Origin("TOKEN1")).apply(FakeRequest("GET", ""))
      redirectLocation(result) should contain("/feedback-survey/ableToDo/TOKEN1")
    }
  }
}
