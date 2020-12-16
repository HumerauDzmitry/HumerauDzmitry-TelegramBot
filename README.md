Telegram Bot Tourist Assistant
===
+ http://t.me/tourist_assistant_by_bot
+ token `1236767246:AAE233wgRBcuN7ShLWHd6ZGZZfrAJ-O-Wjk`

Данный бот выдаёт рекомендацию какую достопримечательность посетить для указанного города.
В белорусском регионе может блокироваться. В этом случае необходимо использовать vpn.
___
**Для запуска необходимо:**

создать базу данных postgres
+    jdbc:postgresql://localhost:5432/tourist_assistant
+    username: postgres
+    password: postgres
+    либо заменить на свою в  `pom.xml` и `application.yaml`

выполнить
`mvn spring-boot:run`

---

очистка миграции
+ `mvn flyway:clean`

миграция базы данных с начальными данными
+ `mvn flyway:migrate`
---
Swagger UI
+ http://localhost:8075/swagger-ui.htm