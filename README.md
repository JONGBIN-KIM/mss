# [MUSINSA] Java(Kotlin) Backend Engineer - 과제

### 과제내용 시각화 페이지

(실행 후 접속) [과제내용 시각화 프론트페이지 링크](http://localhost:8080/musinsa/recommend)  
구현내용 확인 편의를 위해 최소내용으로 프론트페이지를 추가로 개발하였습니다.
시각화페이지에서 데이터 초기셋팅, 간단한 테스트, 그에따른 데이터변화 확인이 가능합니다.   
어플리케이션 실행 후, 해당 페이지에 접속하시어 확인부탁드립니다.  
<br>  
#### * 사용 기술 :  Kotlin, Springboot, H2, SpringCache 
#### * 실행 방법 : 별도의 솔루션설치나 환경설정 없이, 바로 Application Start !  
<br>

### 구현 1. 카테고리 별 최저가 상품
#### API의 효과
* 전시화면에서의 카테고리 별 최저가를 정보하여 최소예산으로 구매 시, 대략의 사용범위를 추측할 수 있다.
* 마케팅이나 프로모션의 요소로 사용할 수 있다 (상의 10000원부터~)       
#### 방향 설정
* 매번 카테고리마다 최저가 상품 산출 시, RDB에 부하 발생. 응답시간 저하
* 카테고리 별 최저가정보를 스프링캐시에 보관하고 갱신하는 방향으로 설정
#### 캐시사용 이유
* 서비스관점에서는 실시간성을 완벽히 보장할정도의 데이터까진 아닐 수 있고, 사전협의로 데이터가 변경되지않을 가능성이 높음
* 현재 요건에서는 캐시사용 시, 구현3번에서도 동시에 활용 가능
* 구현1,구현3의 여러 활용도가 있어, 캐시 사용시 효율을 높일 수 있기에 캐시사용 선택
* 카테고리당 2개의 키 정도만 생기기때문에 메모리 공간을 적게 사용하여 효율을 냄.
* 최저가가 바뀔때만 캐시를 갱신하기때문에 문제발생우려 낮음  
<br>

### 구현 2. 특정 브랜드 모든 카테고리상품 최저가 구매  
#### API의 효과
* 브랜드 기획전이나 브랜드 전용관에서 최저가 표기 가능
* 브랜드 전용쿠폰으로 다양한 전략으로 구매유도가 가능
* 특정 브랜드를 선호할 경우, 예산사용범위 추측 가능
#### 방향 설정
* 브랜드별 카테고리 최저가를 구해야하나, 문제에서 카테고리당 상품1개로 한정되어 RDB로 구현해도 무방
* 이 데이터또한 위와 마찬가지로 실시간성을 완벽히 보장할 필요는 없다고 판단(캐시사용 가능)
* 캐시만료순간 RDB요청 급증을 방지하기위해 PER 알고리즘으로 주기적 갱신(스프링캐시는 만료예정시간을 제공하지않아 TTL체크를 직접구현)
#### 고민 포인트
* 아무리 과제지만 상품 1개로 한정지은 상황만을 고려해도되나 라는 생각에 약 5h의 고민시간소요
* 방향에따라 매우 다양한 아키텍처가 나올 수 있는 문제이기 때문에, 해당 상황만 고려하기로 결정
* 운영환경의 경우 엘라스틱서치를 사용하여 집계와 최저가산출을 효율적으로 활용할 수 있지만, 과제의 특성상 최소한의 환경으로 구현  
  <br>    
### 구현 3. 특정 브랜드 모든 카테고리상품 최저가 구매
#### API의 효과
* 탐색화면에서 카테고리의 가격 범위 제공이 가능하다 (고객의 선택범위 인지)
* 최저가는 구매에도 영향을 주는 데이터. 다양한 용도로 활용가능
#### 방향 설정
* 구현1에서 활용한 카테고리별 최저가에 최고가 정보만 추가하여 구현  
  <br>
### 구현 4. 상품, 브랜드 관리
#### 방향 설정
* 상품, 브랜드의 CRUD 구현
* 상품 추가/수정 시, 캐시에 보관된 카테고리별 최저가와 비교 후 캐시갱신 구현
* 상품 삭제 시, 캐시 재설정



