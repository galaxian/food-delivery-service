# 배달 서비스 프로젝트

## 1️⃣ 프로젝트 설명
<pre>
배달 서비스에 필요한 기능을 생각하여
식당에서 필요한 기능
사용자에게 필요한 기능을 개발
</pre>

</br></br>

## 2️⃣ 프로젝트 요약

* 기간 :  2023.08.19 ~ 2023.09.05
* 개발 언어 : Java
* 개발 라이브러리 : Spring
* DB : MariaDB


## 3️⃣ERD

![erd2](https://github.com/galaxian/food-delivery-service/assets/94841127/afe10571-d1bf-4cd4-9453-9509cdb60ad3)


## 4️⃣ 아키텍처✨

<pre>

</pre>

</br></br>


## 5️⃣ API 명세

| API 기능   | HTTP method | URL                             |
|----------|-------------|---------------------------------|
| 음식 전체 조회 | GET         | /api/v1/restaurants/{id}/foods  |
| 음식 단일 조회 | GET         | /api/v1/foods/{id}              |
| 음식 등록    | POST        | /api/v1/restaurants/{id}/foods  |
| 음식 정보 수정 | PUT         | /api/v1/foods/{id}              |
| 음식 삭제    | DELETE      | /api/v1/foods/{id}              |
 ||||
| 메뉴 전체 조회 | GET         | /api/v1/restaurants/{id}/menus  |
| 메뉴 단일 조회 | GET         | /api/v1/menus/{id}              |
| 메뉴 등록    | POST        | /api/v1/restaurants/{id}/menus  |
| 메뉴 정보 수정 | PUT         | /api/v1/menus/{id}              |
| 메뉴 삭제    | DELETE      | /api/v1/menus/{id}              |
||||
| 주문 전체 조회 | GET         | /api/v1/restaurants/{id}/orders |
| 주문 단일 조회 | GET         | /api/v1/orders/{id}             |
| 주문 등록    | POST        | /api/v1/restaurants/{id}/orders |
| 주문 정보 수정 | PUT         | /api/v1/orders/{id}             |
| 주문 삭제    | DELETE      | /api/v1/orders/{id}             |
||||
| 식당 전체 조회 | GET         | /api/v1/restaurants             |
| 식당 단일 조회 | GET         | /api/v1/restaurants/{id}        |
| 식당 등록    | POST        | /api/v1/restaurants/{id}        |
| 식당 정보 수정 | PUT         | /api/v1/restaurants/{id}        |
| 식당 삭제    | DELETE      | /api/v1/restaurants/{id}        |
||||


