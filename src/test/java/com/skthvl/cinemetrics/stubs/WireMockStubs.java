package com.skthvl.cinemetrics.stubs;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

public class WireMockStubs {
  public static void stubGetMoveDetailsByTitle(final String title, final String year) {
    stubFor(
        get(urlPathEqualTo("/"))
            .withQueryParam("apikey", equalTo("test-api-key"))
            .withQueryParam("t", equalTo(title))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(
                        """
                  {
                    "Title": "%s",
                    "Year": "%s",
                    "Rated": "PG-13",
                    "Released": "16 Jul 2010",
                    "Runtime": "148 min",
                    "BoxOffice": "$292,587,330"
                  }
                """
                            .formatted(title, year))));
  }

  public static void stubGetMoveDetailsByTitleAndYear(final String title, final String year) {
    stubGetMoveDetailsByTitleAndYearByBoxOffice(title, year, "292,587,330");
  }

  public static void stubGetMoveDetailsByTitleAndYearByBoxOffice(
      final String title, final String year, final String boxOffice) {
    stubFor(
        get(urlPathEqualTo("/"))
            .withQueryParam("apikey", equalTo("test-api-key"))
            .withQueryParam("t", equalTo(title))
//            .withQueryParam("y", equalTo(year))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(
                        """
                          {
                            "Title": "%s",
                            "Year": "%s",
                            "Rated": "PG-13",
                            "Released": "16 Jul 2010",
                            "Runtime": "148 min",
                            "BoxOffice": "$%s"
                          }
                        """
                            .formatted(title, year, boxOffice))));
  }
}
