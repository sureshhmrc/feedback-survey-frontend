/*
 * Copyright 2019 HM Revenue & Customs
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

package forms

object FeedbackSurveyEnums {

  trait FeedbackSurveyEnumeration extends Enumeration {
    private lazy val stringValues = values.map(a => a.toString)
    final def isEnumValue(value: String): Boolean = stringValues.contains(value)
  }

  trait BooleanEnumeration extends FeedbackSurveyEnumeration {
    def toBoolean(value: BooleanEnumeration#Value): Option[Boolean]

    final def toBoolean(value: String): Option[Boolean] = {
      if (isEnumValue(value)) {
        toBoolean(this.withName(value))
      } else {
        None
      }
    }
  }

  object BooleanCheckboxEnum extends BooleanEnumeration {
    val True = Value("true")
    val False = Value("false")

    // stable identifiers for usage in match functions
    val TrueString = True.toString
    val FalseString = False.toString

    def toBoolean(value: BooleanEnumeration#Value): Option[Boolean] = value match {
      case True => Some(true)
      case False => Some(false)
      case _ => None
    }
  }

  object BooleanRadioEnum extends BooleanEnumeration {
    val Yes = Value("Yes")
    val No = Value("No")

    // stable identifiers for usage in match functions
    val YesString = Yes.toString
    val NoString = No.toString

    def toBoolean(value: BooleanEnumeration#Value): Option[Boolean] = value match {
      case Yes => Some(true)
      case No => Some(false)
      case _ => None
    }

  }

}
