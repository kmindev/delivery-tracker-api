package com.example.service;

public class QueryFactory {

    public static String searchCarriesQuery() {
        return """
                query CarrierList(
                    $searchText: String!,
                    $after: String
                  ) {
                    carriers(
                      searchText: $searchText,
                      first: 10,
                      after: $after
                    ) {
                      pageInfo {
                        hasNextPage
                        endCursor
                      }
                      edges {
                        node {
                          id
                          name
                        }
                      }
                    }
                  }
                """;
    }

    public static String carriesQuery() {
        return """
                query CarrierList($after: String) {
                  carriers(first: 10, after: $after) {
                    pageInfo {
                      hasNextPage
                      endCursor
                    }
                    edges {
                      node {
                        id
                        name
                      }
                    }
                  }
                }
                """;
    }

    public static String lastEventQuery() {
        return """
                query Track(
                  $carrierId: ID!,
                  $trackingNumber: String!
                ) {
                  track(
                    carrierId: $carrierId,
                    trackingNumber: $trackingNumber
                  ) {
                    lastEvent {
                      time
                      status {
                        code
                      }
                    }
                  }
                }
                """;
    }

    public static String allEventsQuery() {
        return """
                query Track(
                   $carrierId: ID!,
                   $trackingNumber: String!
                 ) {
                   track(
                     carrierId: $carrierId,
                     trackingNumber: $trackingNumber
                   ) {
                     lastEvent {
                       time
                       status {
                         code
                         name
                       }
                       description
                     }
                     events(last: 10) {
                       edges {
                         node {
                           time
                           status {
                             code
                             name
                           }
                           description
                         }
                       }
                     }
                   }
                 }
                """;
    }

    public static String webhookRegisterQuery() {
        return """
                mutation RegisterTrackWebhook(
                    $input: RegisterTrackWebhookInput!
                  ) {
                    registerTrackWebhook(input: $input)
                  }
                """;
    }

}
