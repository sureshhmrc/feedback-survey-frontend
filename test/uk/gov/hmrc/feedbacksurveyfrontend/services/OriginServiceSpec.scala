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

import uk.gov.hmrc.play.binders.Origin
import utils.UnitTestTraits


class OriginServiceSpec extends UnitTestTraits {

  val originService = new OriginService {
    override lazy val originConfigItems = List(
      OriginConfigItem(Some("TOKEN1"), None, List.empty),
      OriginConfigItem(Some("TOKEN2"), Some("http://example.com/custom-feedback-url"), List.empty),
      OriginConfigItem(Some("TOKEN3"), None, List( ("aboutServicePage","thankyouPage") ))
    )
  }

  "The validation of an origin" should {

    "pass with valid origin token" in {

      originService.isValid(Origin("TOKEN1")) shouldBe true
      originService.isValid(Origin("TOKEN2")) shouldBe true
      originService.isValid(Origin("TOKEN3")) shouldBe true
    }

    "fail with an invalid origin" in {

      originService.isValid(Origin("TOKEN4")) shouldBe false
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

  "Parse skip item" should {
    "return a valid list of skip items from 2 components" in {
      val expectedResult = List(
        ("one","two"),
        ("three","four")
      )
      OriginService.parseSkipItem(Some("one->two,three->four")) shouldBe expectedResult
    }

    "return a valid list of skip items from 2 components where one hasn't a valid destination" in {
      val expectedResult = List(
        ("one","two")
      )
      OriginService.parseSkipItem(Some("one->two,three->")) shouldBe expectedResult
    }

    "return a valid list of skip items from 2 components where one hasn't a valid destination but ends in a string" in {
      val expectedResult = List(
        ("one","two")
      )
      OriginService.parseSkipItem(Some("one->two,three-> ")) shouldBe expectedResult
    }

    "return a valid list of skip items from 2 components where one hasn't a valid source and trims off spaces" in {
      val expectedResult = List(
        ("three","four")
      )
      OriginService.parseSkipItem(Some("->two,  three  ->  four  ")) shouldBe expectedResult
    }

    "return a valid list of skip items from 2 components where one has neither source nor destination and spaces before delimiter" in {
      val expectedResult = List(
        ("one","two")
      )
      OriginService.parseSkipItem(Some("one->two,   ->   ")) shouldBe expectedResult
    }

    "return a valid list of skip items from 2 components where one has neither source nor destination" in {
      val expectedResult = List(
        ("one","two")
      )
      OriginService.parseSkipItem(Some("one->two,->")) shouldBe expectedResult
    }

    "return a valid list of skip items from 2 components where one has no delimiter" in {
      val expectedResult = List(
        ("one","two")
      )
      OriginService.parseSkipItem(Some("one->two,bla")) shouldBe expectedResult
    }

    "return a valid list of skip items from None" in {
      val expectedResult = List.empty
      OriginService.parseSkipItem(None) shouldBe expectedResult
    }

    "return a valid list of skip items from a string with no items" in {
      val expectedResult = List.empty
      OriginService.parseSkipItem(Some("")) shouldBe expectedResult
    }
  }

  "skip items for service" should {
    "return the right items for a given service which has skip items" in {
      originService.skipItemsForService("TOKEN3") shouldBe List( ("aboutServicePage","thankyouPage") )
    }
    "return no items for a given service which has no skip items" in {
      originService.skipItemsForService("TOKEN2") shouldBe List.empty
    }
  }
}
