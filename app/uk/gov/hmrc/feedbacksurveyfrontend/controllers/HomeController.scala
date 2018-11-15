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

import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.mvc._
import uk.gov.hmrc.feedbacksurveyfrontend.{AppConfig, FrontendAppConfig, LocalTemplateRenderer}
import uk.gov.hmrc.feedbacksurveyfrontend.services.OriginService
import uk.gov.hmrc.play.binders.Origin
import uk.gov.hmrc.play.frontend.controller.FrontendController
import uk.gov.hmrc.renderer.TemplateRenderer
import utils.FeedbackSurveySessionKeys._

object HomeController extends HomeController {
  val originService = new OriginService
  val appConfig = FrontendAppConfig
}

trait HomeController extends FrontendController  {

  def originService: OriginService
  val appConfig: AppConfig

  implicit val templateRenderer: TemplateRenderer = LocalTemplateRenderer

  def start(origin : Origin): Action[AnyContent] = Action {
    implicit request =>
      if(originService.isValid(Origin(origin.origin))) {
        appConfig.redirectToNewSurveyEnabled match {
          case true =>
            Seq("CARBEN",
              "FANDF",
              "MEDBEN",
              "NISP",
              "P800",
              "PERTAX",
              "PLA",
              "REPAYMENTS",
              "TAI",
              "TCR",
              "TCS",
              "TCSHOME",
              "TES",
              "TYF").contains(origin.origin) match {
                case true => Redirect(s"${appConfig.newFeedbackUrl}/${origin.origin}/pta")
                case false => Redirect(s"${appConfig.newFeedbackUrl}/${origin.origin}/other")
          }
          case false => Redirect(routes.FeedbackSurveyController.ableToDo(origin.origin))
        }
      } else {
        Ok(uk.gov.hmrc.feedbacksurveyfrontend.views.html.error_template("global_errors.title", "global_errors.heading", "global_errors.message"))
      }
  }
}
