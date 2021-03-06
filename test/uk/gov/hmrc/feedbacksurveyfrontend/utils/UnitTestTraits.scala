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

package utils

import com.typesafe.config.ConfigFactory
import org.mockito.Matchers
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.OneServerPerSuite
import play.api.i18n.{Lang, Messages, MessagesApi}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.{Application, Configuration, Environment}
import uk.gov.hmrc.play.test.UnitSpec

import scala.concurrent.Future
import uk.gov.hmrc.http.HeaderCarrier


trait UnitTestTraits extends UnitSpec with MockitoSugar with BeforeAndAfterEach with OneServerPerSuite {

  override implicit lazy val app: Application = new GuiceApplicationBuilder()
    .loadConfig(new Configuration(ConfigFactory.load("application.conf")))
    .build()

  implicit lazy val hc = HeaderCarrier()

  implicit def convertToOption[T, U <: T](value: U): Option[T] = Some(value)

  implicit def convertToFuture[T](value: T): Future[Option[T]] = Future.successful(value)

  implicit def convertToFuture[T](err: Throwable): Future[Option[T]] = Future.failed(err)

  implicit val messagesApi: MessagesApi = app.injector.instanceOf[MessagesApi]

  implicit val messages = Messages(Lang("en"), messagesApi)

  implicit val environment: Environment = app.injector.instanceOf[Environment]

  implicit val configuration: Configuration = app.injector.instanceOf[Configuration]


  // used to help mock setup functions to clarify if certain results should be mocked.
  sealed trait MockConfiguration[+A] {
    final def get = this match {
      case Configure(config) => config
      case _ => throw new RuntimeException("This element is not to be configured")
    }

    final def ifConfiguredThen(action: A => Unit): Unit = this match {
      case Configure(dataToReturn) => action(dataToReturn)
      case _ =>
    }
  }

  case class Configure[A](config: A) extends MockConfiguration[A]

  case object DoNotConfigure extends MockConfiguration[Nothing]

  implicit def convertToMockConfiguration[T](value: T): MockConfiguration[T] = Configure(value)

  implicit def convertToMockConfiguration2[T](value: T): MockConfiguration[Option[T]] = Configure(value)

  implicit def convertToMockConfiguration3[T](value: T): MockConfiguration[Future[T]] = Configure(value)

  implicit def convertToMockConfiguration4[T](value: T): MockConfiguration[Future[Option[T]]] = Configure(Some(value))

  implicit def convertToMockConfiguration5[T](err: Throwable): MockConfiguration[Future[Option[T]]] = Configure(err)

  sealed trait MatcherConfiguration[+A] {
    def matcher: A = this match {
      case AnyMatcher => Matchers.any()
      case EqMatcher(matchValue) => Matchers.eq(matchValue)
    }
  }

  case object AnyMatcher extends MatcherConfiguration[Nothing]

  case class EqMatcher[T](matchValue: T) extends MatcherConfiguration[T]

}
