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

package views.html

import org.joda.time.DateTime
import play.twirl.api.Html

package object helpers {

  implicit def argConv[T](arg: T): Option[T] = Some(arg)

  implicit def stringToHtml(arg: String): Html = Html(arg)

  implicit def stringToHtml2(arg: String): Option[Html] = Html(arg)

  implicit def stringToHtml3(arg: Option[String]): Option[Html] = arg.map(Html(_))

  def theTime(time: DateTime = DateTime.now()): String =
    time.toString("dd MMMM yyyy h:mm ") + time.toString("a").toLowerCase

  def spans(strings: Map[String, Option[String]]): Html =
    Html(
      strings.map {
        case (id, Some(data)) => s"<span id='$id'>$data</span><br/>"
        case _ => ""
      }.view.mkString("")
    )

}
