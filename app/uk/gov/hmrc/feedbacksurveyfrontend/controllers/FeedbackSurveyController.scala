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

import models.feedbackSurveyModels._
import play.api.Play
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import uk.gov.hmrc.feedbacksurveyfrontend.services.OriginService
import uk.gov.hmrc.feedbacksurveyfrontend.views.html
import uk.gov.hmrc.feedbacksurveyfrontend.{FrontendAppConfig, LocalTemplateRenderer}
import uk.gov.hmrc.play.binders.Origin
import uk.gov.hmrc.play.frontend.controller.FrontendController
import uk.gov.hmrc.renderer.TemplateRenderer
import utils.LoggingUtils


object FeedbackSurveyController extends FeedbackSurveyController {
  val originService = new OriginService
}

trait FeedbackSurveyController extends FrontendController with LoggingUtils with I18nSupport {

  implicit val messagesApi: MessagesApi = Play.current.injector.instanceOf[MessagesApi]
  implicit val templateRenderer: TemplateRenderer = LocalTemplateRenderer

  def originService: OriginService

  def mainService(origin: String) = Action { implicit request =>
    Ok(html.feedbackSurvey.mainService(formMappings.mainServiceForm, origin))
  }

  def mainServiceContinue(origin: String): Action[MainService] = Action (parse.form(formMappings.mainServiceForm)) { implicit request =>
    val whatWasTheMainService = request.body.mainService
    val whatWasTheMainServiceOther = request.body.mainServiceOther
    whatWasTheMainServiceOther match {
      case Some(other) => audit(transactionName = "feedback-survey",
        detail = Map("origin" -> origin,
          "whatWasTheMainService" -> whatWasTheMainService.getOrElse(""),
          "whatWasTheMainServiceOther" -> other),
        eventType = eventTypeSuccess)
      case None => audit(transactionName = "feedback-survey",
        detail = Map("origin" -> origin,
          "whatWasTheMainService" -> whatWasTheMainService.getOrElse("")),
        eventType = eventTypeSuccess)
    }
    Redirect(routes.FeedbackSurveyController.ableToDo(origin))
  }

  def mainThing(origin: String) = Action { implicit request =>
    Ok(html.feedbackSurvey.mainThing(formMappings.mainThingForm, origin))
  }

  def mainThingContinue(origin: String): Action[MainThing] = Action (parse.form(formMappings.mainThingForm)) { implicit request =>
    audit(transactionName = "feedback-survey",
      detail = Map("origin" -> origin,
        "whatWastheMainThing" -> request.body.mainThing.getOrElse("")),
      eventType = eventTypeSuccess)
    Redirect(routes.FeedbackSurveyController.ableToDo(origin))
  }

  def ableToDo(origin: String) = Action { implicit request =>
    val backlinkUrl = originService.taxAccount(Origin(origin)) match {
      case Some("BTA") => routes.FeedbackSurveyController.mainService(origin).url
      case None => routes.FeedbackSurveyController.mainThing(origin).url
      }
    Ok(html.feedbackSurvey.ableToDo(formMappings.ableToDoForm, origin, backlinkUrl))
  }

  def ableToDoContinue(origin: String): Action[AbleToDo] = Action (parse.form(formMappings.ableToDoForm)) { implicit request =>
    val ableToDoWhatNeeded = request.body.ableToDoWhatNeeded
    audit(transactionName = "feedback-survey",
      detail = Map("origin" -> origin,
        "ableToDoWhatNeeded" -> ableToDoWhatNeeded.getOrElse("")),
      eventType = eventTypeSuccess)
    Redirect(routes.FeedbackSurveyController.howEasyWasIt(origin))
  }
  
  def howEasyWasIt(origin: String) = Action { implicit request =>
    Ok(html.feedbackSurvey.howEasyWasIt(formMappings.howEasyWasItForm, origin))
  }

  def howEasyWasItContinue(origin: String): Action[HowEasyWasIt] =
    Action (parse.form(formMappings.howEasyWasItForm)) { implicit request =>
      val howEasyWasIt = request.body.howEasyWasIt
      val whyDidYouGiveThisScore = request.body.whyDidYouGiveThisScore
      audit(transactionName = "feedback-survey", detail = Map(
        "origin" -> origin,
        "howEasyWasIt" -> howEasyWasIt.getOrElse(""),
        "whyDidYouGiveThisScore" -> whyDidYouGiveThisScore.getOrElse("")), eventType = eventTypeSuccess)
      Redirect(routes.FeedbackSurveyController.thankYou(Origin(origin)))
    }

  def feelingAboutService(origin: String) = Action { implicit request =>
    Ok(html.feedbackSurvey.feelingAboutService(formMappings.feelingAboutService, origin))
  }

  def usingService(origin: String) = Action { implicit request =>
    Ok(html.feedbackSurvey.usingService(formMappings.usingServiceForm, origin))
  }

  def usingServiceContinue(origin: String): Action[UsingService] = Action (parse.form(formMappings.usingServiceForm)) { implicit request =>
    val beforeUsingThisService = request.body.beforeUsingThisService
    var option0, option1, option2, option3, option4, option5, option6: (String,String) = ("","")
    if (beforeUsingThisService.lift(0).isDefined) {option0 = beforeUsingThisService.lift(0).get -> "Checked"}
    if (beforeUsingThisService.lift(1).isDefined) {option1 = beforeUsingThisService.lift(1).get -> "Checked"}
    if (beforeUsingThisService.lift(2).isDefined) {option2 = beforeUsingThisService.lift(2).get -> "Checked"}
    if (beforeUsingThisService.lift(3).isDefined) {option3 = beforeUsingThisService.lift(3).get -> "Checked"}
    if (beforeUsingThisService.lift(4).isDefined) {option4 = beforeUsingThisService.lift(4).get -> "Checked"}
    if (beforeUsingThisService.lift(5).isDefined) {option5 = beforeUsingThisService.lift(5).get -> "Checked"}
    if (beforeUsingThisService.lift(6).isDefined) {option6 = beforeUsingThisService.lift(6).get -> "Checked"}
    audit(transactionName = "feedback-survey", detail = Map(
      "origin" -> origin,
      option0, option1, option2, option3, option4, option5, option6
    ).filter((t) => t._1 != ""), eventType = eventTypeSuccess)
    Redirect(routes.FeedbackSurveyController.aboutService(origin))
  }

  def aboutService(origin: String) = Action { implicit request =>
    Ok(html.feedbackSurvey.aboutService(formMappings.aboutServiceForm, origin))
  }

  def aboutServiceContinue(origin: String) =
    Action (parse.form(formMappings.aboutServiceForm)) { implicit request =>
    val serviceReceived = request.body.serviceReceived
    audit("feedback-survey", Map("origin" -> origin,
      "serviceReceived" -> serviceReceived.getOrElse("")), eventTypeSuccess)
    Redirect(routes.FeedbackSurveyController.recommendService(origin))
  }

  def recommendService(origin: String) = Action { implicit request =>
    Ok(html.feedbackSurvey.recommendService(formMappings.recommendServiceForm, origin))
  }

  def recommendServiceContinue(origin: String) =
    Action (parse.form(formMappings.recommendServiceForm)) { implicit request =>
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

  def thankYou(origin : Origin): Action[AnyContent] = Action {
    implicit request =>
      if(originService.isValid(Origin(origin.origin))) {
        Ok(html.feedbackSurvey.thankYou(FrontendAppConfig.urLinkUrl, origin.origin))
      } else {
        Ok(html.error_template("global_errors.title", "global_errors.heading", "global_errors.message"))
      }
  }
}
