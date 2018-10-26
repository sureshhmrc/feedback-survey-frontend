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

package models.feedbackSurveyModels

import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages
import play.api.libs.json.Json
import play.api.Play.current
import play.api.i18n.Messages.Implicits._


case class MainService(MainService: Option[String])

object MainService {
  implicit val format = Json.format[MainService]
}

case class MainThing(MainThing: Option[String])

object MainThing {
  implicit val format = Json.format[MainThing]
}

case class AbleToDo(ableToDoWhatNeeded: Option[String])

object AbleToDo {
  implicit val format = Json.format[AbleToDo]
}

case class UsingService(beforeUsingThisService: List[String])

object UsingService {
  implicit val format = Json.format[UsingService]
}

case class AboutService(serviceReceived: Option[String])

object AboutService {
  implicit val format = Json.format[AboutService]
}

case class RecommendService(recommendRating: Option[String],
                            reasonForRating: Option[String])

object RecommendService {
  implicit val format = Json.format[RecommendService]
}


object formMappings {

  val mainServiceForm = Form(mapping(
    "mainService" -> optional(text))(MainService.apply)(MainService.unapply))

  val mainThingForm = Form(mapping(
    "mainThing" -> optional(text))(MainThing.apply)(MainThing.unapply))




  val ableToDoForm = Form(mapping(
    "ableToDoWhatNeeded" -> optional(text))(AbleToDo.apply)(AbleToDo.unapply))

  val usingServiceForm = Form(mapping(
    "beforeUsingThisService" -> list(text.verifying("required field", _.nonEmpty))
  )(UsingService.apply)(UsingService.unapply))

  val aboutServiceForm = Form(mapping(
    "serviceReceived" -> optional(text.verifying("required field", _.nonEmpty)))(AboutService.apply)(AboutService.unapply))

  val recommendServiceForm = Form(mapping(
    "recommendRating" -> optional(text.verifying("required field", _.nonEmpty)),
    "reasonForRating" -> optional(text.verifying("required field", _.nonEmpty)))(RecommendService.apply)(RecommendService.unapply))

  def validInputCharacters(field: String, regXValue: String) = {
    if (field.matches(regXValue)) true else false
  }

}

object formFields {

  val page2Question1Options = Seq(
    ("telephonedHMRC", "feedbackSurvey.page2.question1.option1", Some("checkboxgroup-clear-two checkboxgroup-one")),
    ("wroteToHMRC", "feedbackSurvey.page2.question1.option2", Some("checkboxgroup-clear-two checkboxgroup-one")),
    ("completedAnOnlineForm", "feedbackSurvey.page2.question1.option3", Some("checkboxgroup-clear-two checkboxgroup-one")),
    ("readGuidanceOnGovUK","feedbackSurvey.page2.question1.option4", Some("checkboxgroup-clear-two checkboxgroup-one")),
    ("spokeToYourEmployerAgentOrAccountant", "feedbackSurvey.page2.question1.option5", Some("checkboxgroup-clear-two checkboxgroup-one")),
    ("spokeToAFriendOrFamilyMember", "feedbackSurvey.page2.question1.option6", Some("checkboxgroup-clear-two checkboxgroup-one")),
    ("noneOfThese", "feedbackSurvey.page2.question1.option7", Some("checkboxgroup-clear-one checkboxgroup-two"))
  )

  val page3Question1Options = Seq(
    "5" -> "feedbackSurvey.page3.question1.option1",
    "4" -> "feedbackSurvey.page3.question1.option2",
    "3" -> "feedbackSurvey.page3.question1.option3",
    "2" -> "feedbackSurvey.page3.question1.option4",
    "1" -> "feedbackSurvey.page3.question1.option5"
  )

  val page4Question1Options = Seq(
    "10" -> "feedbackSurvey.page4.question1.option1",
    "9" -> "feedbackSurvey.page4.question1.option2",
    "8" -> "feedbackSurvey.page4.question1.option3",
    "7" -> "feedbackSurvey.page4.question1.option4",
    "6" -> "feedbackSurvey.page4.question1.option5",
    "5" -> "feedbackSurvey.page4.question1.option6",
    "4" -> "feedbackSurvey.page4.question1.option7",
    "3" -> "feedbackSurvey.page4.question1.option8",
    "2" -> "feedbackSurvey.page4.question1.option9",
    "1" -> "feedbackSurvey.page4.question1.option10",
    "0" -> "feedbackSurvey.page4.question1.option11"
  )

}

object fieldValidationPatterns {
  def addresssRegx = """^[A-Za-zÀ-ÿ0-9 &'(),-./]{0,}$"""
  def yesNoRegPattern = "^([1-2]{1})$"
}
