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

package uk.gov.hmrc.feedbacksurveyfrontend.controllers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import controllers.FeedbackSurveyController
import play.api.mvc.{AnyContentAsEmpty, Result}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.feedbacksurveyfrontend.services.{OriginConfigItem, OriginService}
import uk.gov.hmrc.feedbacksurveyfrontend.utils.MockTemplateRenderer
import uk.gov.hmrc.renderer.TemplateRenderer
import utils.FeedbackSurveySessionKeys._
import utils.UnitTestTraits

import scala.concurrent.Future


class FeedbackSurveyControllerSpec extends UnitTestTraits {

  trait SpecSetup {

    implicit val as = ActorSystem()
    implicit val mat = ActorMaterializer()
    val origin = "TOKEN1"

    def testRequest(page: String): FakeRequest[AnyContentAsEmpty.type] =
      FakeRequest(GET, "/feedback-survey/" + s"$page").withSession(sessionOriginService -> origin)
  }

  object TestFeedbackSurveyController extends FeedbackSurveyController {

    override implicit val templateRenderer: TemplateRenderer = MockTemplateRenderer

    val originService = new OriginService {
      override lazy val originConfigItems = List(
        OriginConfigItem(Some("TOKEN1"), None, None),
        OriginConfigItem(Some("TOKEN2"), Some("http://example.com/custom-feedback-url"), None),
        OriginConfigItem(Some("TOKEN3"), None, "BTA")
      )
    }
  }

  "FeedbackSurveyController" should {

    "Go to the mainService page when an origin is from BTA" in new SpecSetup {
      val result: Future[Result] = TestFeedbackSurveyController.mainService(origin = "TOKEN3")(testRequest(page = ""))
      status(await(result)) shouldBe OK
    }

    "redirect to the ableToDo page from mainServiceContinue" in new SpecSetup {
      val result: Future[Result] = TestFeedbackSurveyController.mainServiceContinue(origin = "TOKEN3")(testRequest(page = "")).run()
      status(result) shouldBe SEE_OTHER
      redirectLocation(result).get shouldBe "/feedback-survey/ableToDo/TOKEN3"
    }

    "Go to the mainThing page when an origin is not from BTA" in new SpecSetup {
      val result = TestFeedbackSurveyController.mainThing(origin = "TOKEN1")(testRequest(page =""))
      status(await(result)) shouldBe OK
    }

    "redirect to the ableToDo page from mainThingContinue" in new SpecSetup {
      val result: Future[Result] = TestFeedbackSurveyController.mainServiceContinue(origin = "TOKEN1")(testRequest(page = "")).run()
      status(result) shouldBe SEE_OTHER
      redirectLocation(result).get shouldBe "/feedback-survey/ableToDo/TOKEN1"
    }

    "Go to the ableToDo page" in new SpecSetup {
      val result = TestFeedbackSurveyController.ableToDo(origin = "TOKEN1")(testRequest(page = ""))
      status(await(result)) shouldBe OK
    }

    "redirect to the howEasyWasIt page from ableToDoContinue" in new SpecSetup {
      val result: Future[Result] = TestFeedbackSurveyController.ableToDoContinue(origin = "TOKEN1")(testRequest(page = "")).run()
      status(result) shouldBe SEE_OTHER
      redirectLocation(result).get shouldBe "/feedback-survey/howEasyWasIt/TOKEN1"
    }

    "Go to the howEasyWasIt page" in new SpecSetup {
      val result = TestFeedbackSurveyController.howEasyWasIt(origin = "TOKEN1")(testRequest(page = ""))
      status(await(result)) shouldBe OK
    }

    "redirect to the howDidYouFeel page from howEasyWasItContinue" in new SpecSetup {
      val result: Future[Result] = TestFeedbackSurveyController.howEasyWasItContinue(origin = "TOKEN1")(testRequest(page = "")).run()
      status(result) shouldBe SEE_OTHER
      redirectLocation(result).get shouldBe "/feedback-survey/howDidYouFeel/TOKEN1"
    }

    "Go to the howDidYouFeel page" in new SpecSetup {
      val result = TestFeedbackSurveyController.howDidYouFeel(origin = "TOKEN1")(testRequest(page = ""))
      status(await(result)) shouldBe OK
    }

    "redirect to the Thank you page from howDidYouFeelContinue when this origin does not have a custom feedback url" in new SpecSetup {
      val result = TestFeedbackSurveyController.howDidYouFeelContinue(origin = "TOKEN1")(testRequest(page = "")).run()
      status(result) shouldBe SEE_OTHER
      redirectLocation(result).get should include("/feedback-survey/thankYou?origin=TOKEN1")
    }

    "redirect to the custom feedback url from howDidYouFeelContinue when this origin has a custom feedback url" in new SpecSetup {
      val result = TestFeedbackSurveyController.howDidYouFeelContinue(origin = "TOKEN2")(testRequest(page = "")).run()
      status(result) shouldBe SEE_OTHER
      redirectLocation(result).get shouldBe "http://example.com/custom-feedback-url"
    }

    "Go to the Thank you page " in new SpecSetup {
      val result = TestFeedbackSurveyController.thankYou(origin = "TOKEN1")(testRequest(page = "thankYou"))
      status(result) shouldBe OK
    }
  }
}
