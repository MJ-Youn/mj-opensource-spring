# mj-opensource-spring
 - Spring 개발을 진행하면서 필요한 유틸성 기능을 추가 개발하는 유틸용 library


## release note
### 0.1.0 - 20211227
 + DB 개발(`JDBCTemplate`)을 위한 유틸 추가
   - `GenericEntity`: Entity
   - `GenericRepository`: Repository 
   - `PostgreSQLRepository`: PostgreSQL 사용을 위한 Repository

### 0.1.1-SNAPSHOT - 20211227
 + JdbcUtils 추가
   - nullable setter/getter 추가 
   - `getColumnsStartsWith` 추가

### 0.1.1-SNAPSHOT - 20211228
 + GenericService 추가

### 0.1.2 - 20220110
+ optional validation 기능 추가

### 0.1.3 - 20220110
+ GenericRepository 고도화
  - Parameter Map을 통한 데이터 조회 기능 개발 

### 0.1.4 - 20220111
+ ListResultConverter 고도화
  - page 변환 함수 추가 

### 0.1.5 - 20220111
+ GenericRepository 오류 수정

### 0.1.5-SNAPSHOT - 20220111
+ GenericRepository 오류 수정

### 0.1.6 - 20220117
+ 배포를 위한 버전 변경

### 0.1.7 - 20220119
+ GenericRepository 고도화

### 0.1.8 - 20220119
+ JDBCUtils 고도화
  - inner join 함수 추가 

### 0.1.9 - 20220119
+ GenericRepository 고도화
  - execute 함수 추가

### 0.2.0 - 20220315
+ MariadbRepository 추가

### 0.2.1 - 20220315
+ PageResultConverter에 map() 함수 추가

### 0.2.2 - 20220401
+ jackson library의 버전 문제 수정

### 0.2.3 - ?

### 0.2.4 - 20220609
+ ListResultConverter에 iterable converter 추가

### 0.2.5 - 20220613
+ validated를 위한 marker class 추가

### 0.2.6 - 20220614
+ validated를 위한 marker class를 interface로 변경
