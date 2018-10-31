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
      Redirect(routes.FeedbackSurveyController.thankYou(origin))
    }

//  def feelingAboutService(origin: String) = Action { implicit request =>
//    Ok(html.feedbackSurvey.feelingAboutService(formMappings.feelingAboutService, origin))
//  }

  def thankYou(origin: String): Action[AnyContent] = Action {
    implicit request =>
      if(originService.isValid(Origin(origin))) {
        Ok(html.feedbackSurvey.thankYou(FrontendAppConfig.urLinkUrl, origin))
      } else {
        Ok(html.error_template("global_errors.title", "global_errors.heading", "global_errors.message"))
      }
  }
}
