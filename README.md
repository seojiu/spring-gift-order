# spring-gift-order

## step1 : 카카오 로그인

### 기능 요구 사항
### : 카카오 로그인을 통해 인가 코드를 받고, 인가 코드를 사용해 토큰을 받은 후 향후 카카오 API 사용을 준비한다.

#### - 카카오계정 로그인을 통해 인증 코드를 받는다.
#### - 토큰 받기를 읽고 액세스 토큰을 추출한다.
#### - 앱 키, 인가 코드가 절대 유출되지 않도록 한다.
#### - 특히 시크릿 키는 GitHub나 클라이언트 코드 등 외부에서 볼 수 있는 곳에 추가하지 않는다.

 

### - HTTP Client
#####  REST Clients
#####  사용할 클라이언트를 선택할 때 어떤 기준을 사용해야 할까?
#####  클라이언트 인스턴스를 효과적으로 생성/관리하는 방법은 무엇인가?
###
### - 더 적절한 테스트 방법이 있을까?
####  요청을 보내고 응답을 파싱하는 부분만 테스트하려면 어떻게 해야 할까?
#####  비즈니스 로직에 연결할 때 단위/통합 테스트는 어떻게 할까?
###
### - API 호출에 문제가 발생하면 어떻게 할까?
#####  응답 시간이 길면 어떻게 할까? 몇 초가 적당할까?
#####  오류 코드는 어떻게 처리해야 할까?
#####  응답 값을 파싱할 때 문제가 발생하면 어떻게 할까?
