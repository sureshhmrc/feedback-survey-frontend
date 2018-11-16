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

package uk.gov.hmrc.feedbacksurveyfrontend

import play.api.Play.{configuration, current}
import uk.gov.hmrc.play.config.ServicesConfig

trait AppConfig {
  val analyticsToken: Option[String]
  val analyticsHost: String
  val reportAProblemPartialUrl: String
  val deskproToken: Option[String]
  val urLinkUrl: Option[String]
  val redirectToNewSurveyEnabled: Boolean
  val newFeedbackUrl: String
}

object FrontendAppConfig extends AppConfig with ServicesConfig {

  private lazy val contactFrontendBaseUrl = getConfString("contact-frontend.external-url", "")
  private val contactFrontendService = baseUrl("contact-frontend")
  private val contactFormServiceIdentifier = "FEEDBACK-SURVEY"
  private lazy val newFeedbackBaseUrl = baseUrl("feedback")

  lazy val frontendTemplatePath = configuration.getString("microservice.services.frontend-template-provider.path").getOrElse("/templates/mustache")

  override lazy val analyticsToken = configuration.getString("google-analytics.token")
  override lazy val analyticsHost = configuration.getString("google-analytics.host").getOrElse("service.gov.uk")
  override lazy val reportAProblemPartialUrl = s"$contactFrontendService/contact/problem_reports?secure=true"
  override lazy val deskproToken = configuration.getString("deskproToken")
  override lazy val urLinkUrl = configuration.getString("feature.ur-link.url")
  override lazy val redirectToNewSurveyEnabled = configuration.getBoolean("feature.redirect-to-new-survey").getOrElse(false)
  override lazy val newFeedbackUrl = s"$newFeedbackBaseUrl${configuration.getString("microservice.services.feedback.path").getOrElse("/feedback")}"
}
