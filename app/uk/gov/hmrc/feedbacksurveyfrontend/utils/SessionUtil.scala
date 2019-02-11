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

import play.api.mvc.{AnyContent, Request, Result}

object SessionUtil {

  implicit class SessionUtilForRequest(request: Request[AnyContent]) {
    @inline def getServiceOrigin: Option[String] = request.session.get(FeedbackSurveySessionKeys.sessionOriginService)
  }

  implicit class SessionUtilForResult(result: Result) {
    @inline def addServiceOriginToSession(originService: String)(implicit request: Request[AnyContent]): Result =
          result.addingToSession(FeedbackSurveySessionKeys.sessionOriginService -> originService)
  }
  // cached implicit that can be used elsewhere
  // e.g. implicit val sessionUtil = SessionUtil.sessionUtil
  implicit val sessionUtilForRequest = (request: Request[AnyContent]) => new SessionUtil.SessionUtilForRequest(request)
  implicit val sessionUtilForResult = (result: Result) => new SessionUtil.SessionUtilForResult(result)
}

object FeedbackSurveySessionKeys {
  val sessionOriginService = "originService"
}
