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

package uk.gov.hmrc.feedbacksurveyfrontend.utils

import uk.gov.hmrc.feedbacksurveyfrontend.AppConfig

class MockAppConfig(newSurveyFeatureEnabled: Boolean = false, newSurveyUrl: String = "") extends AppConfig {
  override val analyticsToken: Option[String] = None
  override val analyticsHost: String = ""
  override val reportAProblemPartialUrl: String = ""
  override val deskproToken: Option[String] = None
  override val urLinkUrl: Option[String] = None
  override val redirectToNewSurveyEnabled: Boolean = newSurveyFeatureEnabled
  override val newFeedbackUrl: String = newSurveyUrl
}
