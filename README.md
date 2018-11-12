# SpringAPIGateway-ConfigServer
Zuul GateWay server + Dynamic Configuration Server


# Gateway Zuul Server
## summary
- spring boot(2.4)+ netflix zuul Server 구현
- configuration에 등록된 URL만 허용하며 해당 규칙에 맞게 routing 한다
## 주요 소스
- com.kdpark.zuul.filter : pre, post filter (GW에 요청이 왔을때 라우팅 전처리/라우팅/ 리턴 로직을 설정한다)
- src/main/resources/application.yml : zuul설정 / configuration 위치 설정


# Configuraion Server
## summary
- Spring cloud config server 구현
- DB에서 routing 설정정보를 읽어와 동적으로 gateway에 설정을 한다
## 주요 소스
- application.yml : config서버 설정, cloud.config.server.jdbc.sql (DB에서 routing 정보를 가져오는 query)
- /src/main/resources/db/script.sql : routing 정보 table / insert 예시
