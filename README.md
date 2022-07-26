# jdbc-metadata-learning
The purpose of this repository is to test implementation of java.sql.DatabaseMetaData of each JDBC driver.

## Usage
### Setup
setup database for test.
```
docker-compose up -d
```

### Test
run test
```
./gradlew cleanTest test
```

## Test Results
### Postgresql
<img width="820" alt="스크린샷 2022-07-24 오후 9 03 20" src="https://user-images.githubusercontent.com/13404470/180647384-f000df56-3f71-44c5-8a25-46a385c7fd09.png">

### Mysql
<img width="760" alt="스크린샷 2022-07-24 오후 10 23 49" src="https://user-images.githubusercontent.com/13404470/180649081-336ddae1-3bd0-443a-aa8e-79673ad31633.png">

### Hsql
<img width="820" alt="스크린샷 2022-07-24 오후 9 00 23" src="https://user-images.githubusercontent.com/13404470/180647416-6650668e-3769-471f-91ce-18c50605f891.png">

### Mssql
![화면 캡처 2022-07-25 160601](https://user-images.githubusercontent.com/13404470/180718190-4ed4b038-974d-4f57-8b13-acdfec53d075.png)

### Mariadb
![화면 캡처 2022-07-25 160748](https://user-images.githubusercontent.com/13404470/180718352-5f385539-4899-4cc7-82c1-2da3843d3d74.png)

