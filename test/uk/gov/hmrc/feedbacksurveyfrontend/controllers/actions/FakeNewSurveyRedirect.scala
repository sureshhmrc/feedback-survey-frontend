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

import play.api.mvc.{Action, AnyContent, Request, Result}
import uk.gov.hmrc.feedbacksurveyfrontend.AppConfig

object FakeNewSurveyRedirect extends NewSurveyRedirect {
  override def appConfig: AppConfig = ???

  override def apply(origin: String)(f: Request[AnyContent] => Result): Action[AnyContent] = Action(f(_))
}