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

import controllers.actions.NewSurveyRedirect
import models.feedbackSurveyModels._
import play.api.Play
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import uk.gov.hmrc.feedbacksurveyfrontend.{FrontendAppConfig, LocalTemplateRenderer}
import uk.gov.hmrc.feedbacksurveyfrontend.services.OriginService
import uk.gov.hmrc.feedbacksurveyfrontend.views.html
import uk.gov.hmrc.play.binders.Origin
import uk.gov.hmrc.play.frontend.controller.FrontendController
import uk.gov.hmrc.renderer.TemplateRenderer
import utils.LoggingUtils


object FeedbackSurveyController extends FeedbackSurveyController {
  val originService = new OriginService
  val newSurveyRedirect: NewSurveyRedirect = NewSurveyRedirect
}

trait FeedbackSurveyController extends FrontendController with LoggingUtils with I18nSupport {

  implicit val messagesApi: MessagesApi = Play.current.injector.instanceOf[MessagesApi]
  implicit val templateRenderer: TemplateRenderer = LocalTemplateRenderer

  def originService: OriginService
  val newSurveyRedirect: NewSurveyRedirect

  def ableToDo(origin: String) = newSurveyRedirect(origin) { implicit request =>
    Ok(html.feedbackSurvey.ableToDo(formMappings.ableToDoForm, origin))
  }

  def ableToDoContinue(origin: String) =  Action (parse.form(formMappings.ableToDoForm)) { implicit request =>
        val ableToDoWhatNeeded = request.body.ableToDoWhatNeeded
    audit("feedback-survey", Map("origin" -> origin,
      "ableToDoWhatNeeded" -> ableToDoWhatNeeded.getOrElse("")), eventTypeSuccess)
    Redirect(routes.FeedbackSurveyController.usingService(origin))
  }

  def usingService(origin: String) = newSurveyRedirect(origin) { implicit request =>
    Ok(html.feedbackSurvey.usingService(formMappings.usingServiceForm, origin))
  }

  def usingServiceContinue(origin: String) = Action (parse.form(formMappings.usingServiceForm)) { implicit request =>
    val beforeUsingThisService = request.body.beforeUsingThisService
    var option0, option1, option2, option3, option4, option5, option6: (String,String) = ("","")
    if (beforeUsingThisService.lift(0).isDefined) {option0 = beforeUsingThisService.lift(0).get -> "Checked"}
    if (beforeUsingThisService.lift(1).isDefined) {option1 = beforeUsingThisService.lift(1).get -> "Checked"}
    if (beforeUsingThisService.lift(2).isDefined) {option2 = beforeUsingThisService.lift(2).get -> "Checked"}
    if (beforeUsingThisService.lift(3).isDefined) {option3 = beforeUsingThisService.lift(3).get -> "Checked"}
    if (beforeUsingThisService.lift(4).isDefined) {option4 = beforeUsingThisService.lift(4).get -> "Checked"}
    if (beforeUsingThisService.lift(5).isDefined) {option5 = beforeUsingThisService.lift(5).get -> "Checked"}
    if (beforeUsingThisService.lift(6).isDefined) {option6 = beforeUsingThisService.lift(6).get -> "Checked"}
    audit("feedback-survey", Map(
      "origin" -> origin,
      option0, option1, option2, option3, option4, option5, option6
    ).filter((t) => t._1 != ""), eventTypeSuccess)
    Redirect(routes.FeedbackSurveyController.aboutService(origin))
  }

  def aboutService(origin: String) = newSurveyRedirect(origin) { implicit request =>
    Ok(html.feedbackSurvey.aboutService(formMappings.aboutServiceForm, origin))
  }

  def aboutServiceContinue(origin: String) =  Action (parse.form(formMappings.aboutServiceForm)) { implicit request =>
    val serviceReceived = request.body.serviceReceived
    audit("feedback-survey", Map("origin" -> origin,
      "serviceReceived" -> serviceReceived.getOrElse("")), eventTypeSuccess)
    Redirect(routes.FeedbackSurveyController.recommendService(origin))
  }

  def recommendService(origin: String) = newSurveyRedirect(origin) { implicit request =>
    Ok(html.feedbackSurvey.recommendService(formMappings.recommendServiceForm, origin))
  }

  def recommendServiceContinue(origin: String) =  Action (parse.form(formMappings.recommendServiceForm)) { implicit request =>
    val reasonForRating = request.body.reasonForRating
    val recommendRating = request.body.recommendRating
    audit("feedback-survey", Map(
      "origin" -> origin,
      "reasonForRating" -> reasonForRating.getOrElse(""),
      "recommendRating" -> recommendRating.getOrElse("")), eventTypeSuccess)

    originService.customFeedbackUrl(Origin(origin)) match {
      case Some(x) => Redirect(x)
      case None => Redirect(routes.FeedbackSurveyController.thankYou(Origin(origin)))
    }
  }

  def thankYou(origin : Origin): Action[AnyContent] = newSurveyRedirect(origin.origin) {
    implicit request =>
      if(originService.isValid(Origin(origin.origin))) {
        Ok(html.feedbackSurvey.thankYou(FrontendAppConfig.urLinkUrl, origin.origin))
      } else {
        Ok(html.error_template("global_errors.title", "global_errors.heading", "global_errors.message"))
      }
  }

}
