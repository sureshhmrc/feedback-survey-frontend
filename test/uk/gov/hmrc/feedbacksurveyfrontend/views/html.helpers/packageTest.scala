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

package views.html.helpers
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.jsoup.nodes.Document
import utils.{UnitTestTraits, HtmlUtils}

class packageTest extends UnitTestTraits with HtmlUtils {

  // this is shadowed so we would use the implicit conversion defined in the package instead
  override def convertToOption[T, U <: T](value: U): Option[T] = ???

  "theTime function" should {
    val testDateFormat = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss")
    "format the time correctly" in {
      val amTime = DateTime.parse("04/02/2011 08:27:05", testDateFormat)
      val pmTime = DateTime.parse("04/12/2011 20:27:05", testDateFormat)
      theTime(amTime) shouldBe "04 February 2011 8:27 am"
      theTime(pmTime) shouldBe "04 December 2011 8:27 pm"
    }
  }

  "spans function" should {
    "Output only defined elements in <span> tags" in {
      val testData: Map[String, Option[String]] = Map[String, Option[String]]("addressLine1" -> "line1", "addressLine2" -> "line2", "addressLine3" -> None, "addressLine4" -> "line4")
      val soupDoc: Document = spans(testData)
      val spanTags = soupDoc.getElementsByTag("span")
      withClue(s"span tags found:\n$spanTags\n") {
        spanTags.size() shouldBe 3
      }
      testData.foreach {
        case (id: String, Some(x)) =>
          spanTags.text().contains(x) shouldBe true
        case _ =>
      }
    }
  }


}
