package performance

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.springframework.http.HttpStatus

import scala.concurrent.duration._

class BasicScript extends Simulation {

    val httpProtocol = http
      .baseUrl("http://localhost:8082/only-resource") // Here is the root for all relative URLs
      .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
      .acceptEncodingHeader("gzip, deflate")
      .acceptLanguageHeader("en-US,en;q=0.5")
      .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

    val scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
      .exec(http("request_1")
        .get("/1").check(status.is(HttpStatus.OK.value())))
      .pause(10)
      .exec(http("request_2")
        .get("/0").check(status.is(HttpStatus.OK.value())))

    setUp(scn.inject(atOnceUsers(100)).protocols(httpProtocol))
}
