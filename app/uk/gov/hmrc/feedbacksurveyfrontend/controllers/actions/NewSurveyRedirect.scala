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
import uk.gov.hmrc.feedbacksurveyfrontend.{AppConfig, FrontendAppConfig}
import uk.gov.hmrc.play.binders.Origin
import play.api.mvc.Results._

object NewSurveyRedirect extends NewSurveyRedirect {
  val appConfig: AppConfig = FrontendAppConfig
}

trait NewSurveyRedirect {

  val appConfig: AppConfig

  private val ptaRedirects =
    Seq(
      "CARBEN",
      "FANDF",
      "MEDBEN",
      "NISP",
      "P800",
      "PERTAX",
      "REPAYMENTS",
      "PLA",
      "TAI",
      "TCR",
      "TCS",
      "TCSHOME",
      "TES",
      "TYF")

  def apply(origin: String)(f: Request[AnyContent] => Result): Action[AnyContent] =
    Action { implicit request =>
      if (!appConfig.redirectToNewSurveyEnabled) {
        f(request)
      }
      else {
        if (ptaRedirects.contains(origin))
          Redirect(s"${appConfig.newFeedbackUrl}/$origin/personal")
        else
          Redirect(s"${appConfig.newFeedbackUrl}/$origin")
      }
    }
}