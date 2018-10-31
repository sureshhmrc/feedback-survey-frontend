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
import play.api.libs.json.Json


case class MainService(mainService: Option[String], mainServiceOther: Option[String])

object MainService {
  implicit val format = Json.format[MainService]
}

case class MainThing(mainThing: Option[String])

object MainThing {
  implicit val format = Json.format[MainThing]
}

case class AbleToDo(ableToDoWhatNeeded: Option[String])

object AbleToDo {
  implicit val format = Json.format[AbleToDo]
}

case class HowEasyWasIt(howEasyWasIt: Option[String],
                        whyDidYouGiveThisScore: Option[String])

object HowEasyWasIt {
  implicit val format = Json.format[HowEasyWasIt]
}

case class FeelingAboutService(feelingAboutService: Option[String])

object FeelingAboutService {
  implicit val format = Json.format[FeelingAboutService]
}


object formMappings {

  val mainServiceForm = Form(mapping(
    "mainService" -> optional(text),
    "mainServiceOther" -> optional(text)
  )(MainService.apply)(MainService.unapply))

  val mainThingForm = Form(mapping(
    "mainThing" -> optional(text))(MainThing.apply)(MainThing.unapply))

  val ableToDoForm = Form(mapping(
    "ableToDoWhatNeeded" -> optional(text))(AbleToDo.apply)(AbleToDo.unapply))

  val howEasyWasItForm = Form(mapping(
    "howEasyWasIt" -> optional(text),
    "whyDidYouGiveThisScore" -> optional(text))(HowEasyWasIt.apply)(HowEasyWasIt.unapply))

  val feelingAboutServiceForm = Form(mapping(
    "feelingAboutService" -> optional(text))(FeelingAboutService.apply)(FeelingAboutService.unapply))

  def validInputCharacters(field: String, regXValue: String) = {
    if (field.matches(regXValue)) true else false
  }
}

object fieldValidationPatterns {
  def addresssRegx = """^[A-Za-zÀ-ÿ0-9 &'(),-./]{0,}$"""
  def yesNoRegPattern = "^([1-2]{1})$"
}
