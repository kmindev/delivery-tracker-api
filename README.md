# Delivery Tracker API 사용 예제

## 1. 택배사 검색 및 조회

설명

* 텍스트로 택배사 정보(id, name)을 조회한다.

<details>
<summary>요청/응답 예시</summary>

* 요청 예시

```
POST /graphql HTTP/1.1
Host: apis.tracker.delivery
Content-Type: application/json
Authorization: TRACKQL-API-KEY [YOUR_CLIENT_ID]:[YOUR_CLIENT_SECRET]

{
  "query": "query CarrierList(\n  $searchText: String!,\n  $after: String\n) {\n  carriers(\n    searchText: $searchText,\n    first: 10,\n    after: $after\n  ) {\n    pageInfo {\n      hasNextPage\n      endCursor\n    }\n    edges {\n      node {\n        id\n        name\n      }\n    }\n  }\n}",
  "variables": {
    "searchText": "우체국",
    "after": null
  }
}
```

* 응답 예시

```
{
  "data": {
    "carriers": {
      "pageInfo": {
        "hasNextPage": false,
        "endCursor": "eyJpbmRleCI6OX0="
      },
      "edges": [
        {
          "node": {
            "id": "kr.epost",
            "name": "우체국택배"
          }
        },
        {
          "node": {
            "id": "kr.epost.ems",
            "name": "우체국택배 국제우편(EMS)"
          }
        },
        {
          "node": {
            "id": "un.upu.ems",
            "name": "EMS"
          }
        }
      ]
    }
  }
}
```

</details>

## 2. 운송장 마지막 상태 조회

설명

* 택배사 ID와 운송장 번호를 전달하여 운송장 마지막 상태를 조회한다.
  주의점
*
    * 일반적인 경우 1초 미만으로 응답을 제공하며, 권장 Timeout 은 최대 15 초이다.

<details>
<summary>요청/응답 예시</summary>

* 요청 예시

```
POST /graphql HTTP/1.1
Host: apis.tracker.delivery
Content-Type: application/json
Authorization: TRACKQL-API-KEY [YOUR_CLIENT_ID]:[YOUR_CLIENT_SECRET]

{
  "query": "query Track(\n  $carrierId: ID!,\n  $trackingNumber: String!\n) {\n  track(\n    carrierId: $carrierId,\n    trackingNumber: $trackingNumber\n  ) {\n    lastEvent {\n      time\n      status {\n        code\n      }\n    }\n  }\n}",
  "variables": {
    "carrierId": "kr.cjlogistics",
    "trackingNumber": "1234567890"
  }
}
```

* 응답 예시

```
{
  "data": {
    "track": {
      "lastEvent": {
        "time": "2025-04-15T06:33:30.000+09:00",
        "status": {
          "code": "DELIVERED"
        }
      }
    }
  }
}
```

</details>

## 3. 운송장 전체 상태 조회

설명

* 택배사 ID와 운송장 번호를 전달하여 운송장 전체 상태를 조회한다.

주의점

* 10개 이상의 이벤트가 필요한 경우 Pagination 을 통해 더 많은 이벤트를 조회할 수 있습니다.
* `TrackInfo`에는 발송자 정보 등의 문서에 언급되지 않은 다양한 정보들을 포함하고 있다.

<details>
<summary>요청/응답 예시</summary>

* 요청 예시

```
POST /graphql HTTP/1.1
Host: apis.tracker.delivery
Content-Type: application/json
Authorization: TRACKQL-API-KEY [YOUR_CLIENT_ID]:[YOUR_CLIENT_SECRET]

{
  "query": "query Track(\n  $carrierId: ID!,\n  $trackingNumber: String!\n) {\n  track(\n    carrierId: $carrierId,\n    trackingNumber: $trackingNumber\n  ) {\n    lastEvent {\n      time\n      status {\n        code\n        name\n      }\n      description\n    }\n    events(last: 10) {\n      edges {\n        node {\n          time\n          status {\n            code\n            name\n          }\n          description\n        }\n      }\n    }\n  }\n}",
  "variables": {
    "carrierId": "kr.cjlogistics",
    "trackingNumber": "1234567890"
  }
}
```

* 응답 예시

```
{
  "data": {
    "track": {
      "lastEvent": {
        "time": "2025-04-15T06:33:30.000+09:00",
        "status": {
          "code": "DELIVERED",
          "name": "배송완료"
        },
        "description": "배송완료 - 대전1"
      },
      "events": {
        "edges": [
          {
            "node": {
              "time": "2025-04-14T21:22:38.000+09:00",
              "status": {
                "code": "IN_TRANSIT",
                "name": "집하"
              },
              "description": "집하 - 대전1HUB"
            }
          },
          {
            "node": {
              "time": "2025-04-14T21:23:07.000+09:00",
              "status": {
                "code": "IN_TRANSIT",
                "name": "캠프상차"
              },
              "description": "캠프상차 - 대전1HUB"
            }
          },
          {
            "node": {
              "time": "2025-04-14T22:44:52.000+09:00",
              "status": {
                "code": "IN_TRANSIT",
                "name": "캠프도착"
              },
              "description": "캠프도착 - 대전1"
            }
          },
          {
            "node": {
              "time": "2025-04-15T01:05:54.000+09:00",
              "status": {
                "code": "OUT_FOR_DELIVERY",
                "name": "배송출발"
              },
              "description": "배송출발 - 대전1"
            }
          },
          {
            "node": {
              "time": "2025-04-15T06:33:30.000+09:00",
              "status": {
                "code": "DELIVERED",
                "name": "배송완료"
              },
              "description": "배송완료 - 대전1"
            }
          }
        ]
      }
    }
  }
}
```

</details>

## 4. Tracking Webhook 등록

설명

* 택배사 ID와 운송장 번호를 전달하여 운송장을 추적할 수 있도록 콜백 URL을 등록한다.
* 운송장 상태가 변경 되었을 때 등록된 콜백 URL로 `request`를 전달한다.
* 등록을 해체할 때는 `expirationTime`을 현재시간으로 요청을 보낸다.(권장하는 방법은 아님.)
* 콜백 URL로 요청이 온 `requets` 내부에는 운송장 상태에 대한 정보는 따로 없기 때문에 `Query.track`으로 운송장 상태를 조회해야 한다.
* 내부적으로 `Query.track`을 폴링하는 방법보다 `webhook`을 사용하는 것이 효율적이다.(data가 캐싱되어 있기 때문)
* `expirationTime`에 도달하지 않는 한, 운송장 상태가 변경되는 경우 Callback 발생이 보장
* 콜백 Url 의 정보만 알면 누구나 호출할 수 있기 때문에 딜리버리 트래커 서버에서 보낸 데이터라는 것을 신뢰할 수 없습니다.

주의점

* 콜백 URL 등록시 `https`만 가능하다. 
* `expirationTime` 48시간 뒤의 시간을 사용하는 것을 권장

<details>
<summary>요청/응답 예시</summary>

* 요청 예시

```
POST /graphql HTTP/1.1
Host: apis.tracker.delivery
Content-Type: application/json
Authorization: TRACKQL-API-KEY [YOUR_CLIENT_ID]:[YOUR_CLIENT_SECRET]

{
  "query": "mutation RegisterTrackWebhook(\n  $input: RegisterTrackWebhookInput!\n) {\n  registerTrackWebhook(input: $input)\n}",
  "variables": {
    "input": {
      "carrierId": "kr.epost",
      "trackingNumber": "1234567890",
      "callbackUrl": "https://webhook.site/0e30dc0d-100b-4bd1-86c3-c7d8b0d48453",
      "expirationTime": "2023-01-01T00:00:00Z"
    }
  }
}
```

* 응답 예시

```
{
  "data": {
    "registerTrackWebhook": true
  }
}
```

</details>

## 5. 콜백 처리

설명

* 운송장 상태가 변경되었을 때 등록된 콜백 URL를 `request`를 전달한다.
  ```
  POST {callbackUrl}
  content-type: application/json
      
  {
    "carrierId": "{carrierId}",
    "trackingNumber": "{trackingNumber}"
  }
  ```
* 전달받은 `request`로 `Query.track`으로 정보를 조회하도록 한다.

주의점

* 처리하는 방식으로는 `핸들러 내에서 직접 처리`, `Callback Queue` 방법이 있다.
    * 트래픽이 많은 경우 `핸들러 내에서 직접 처리`방법이 `Callback Queue`방법에 비해 상대적으로 Timeout 과 RateLimit 에 대한 복잡성을 키울 수 있습니다.
* Callback 구현은 Response 를 1초 내에 전달하고 Response Status Code 로 202 (Accepted) 를 보내실 것을 권장
    * retry 로직으로 인해 계속해서 재시도하기 때문

## 참고 자료

* https://tracker.delivery/docs/try