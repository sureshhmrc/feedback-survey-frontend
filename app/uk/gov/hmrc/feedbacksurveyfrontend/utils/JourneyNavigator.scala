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

object JourneyNavigator {
  val ableToDoPage = "ableToDoPage"
  val usingServicePage = "usingServicePage"
  val aboutServicePage = "aboutServicePage"
  val recommendServicePage = "recommendServicePage"
  val thankyouPage = "thankyouPage"

  def nextPage(origin: String, page:String): String = {
    (page, origin) match {
      case (`ableToDoPage`, _) => usingServicePage
      case (`usingServicePage`, _) => aboutServicePage
      case (`aboutServicePage`, "PODS") => thankyouPage
      case (`aboutServicePage`, _) => recommendServicePage
      case (`recommendServicePage`, _) => thankyouPage
      case _ => usingServicePage
    }
  }
}
