## CS Study Application

### 개요

플래시 카드를 활용한 학습용 웹 어플리케이션

* flow chart

  ![그림2](https://user-images.githubusercontent.com/70496139/167802742-31e32850-619b-4e32-a0ac-fed0bea1aa09.png)

<br>

### 개발 환경

|**IDE**| **Intellij** |
| :--- | :------------------- |
| **Language**  | **Java11** |
| **Framework** | **Spring (Spring Boot)** |
|  **Library**  | **Thymeleaf, Flyway, Lombok, JCache** |
|   **DBMS**    | **mariaDB** |

<br>

### 사용방법(ubuntu)

#### 1. DB 세팅

* 설치 명령어

  ```shell
  # MariaDB 설치
  sudo apt-get install mariadb-server
  ```

* 설치 후 DB 생성

  ```shell
  # MariaDB 실행
  sudo mariadb
  ```

  ```mariadb
  # sample DB 생성
  create database cardgame;
  ```

* 비밀번호 설정 후 MariaDB 종료

  ```mariadb
  # 비밀번호 설정
  set password for root@'localhost' = password('<DB 비밀번호>');
  # MariaDB 종료
  exit
  ```

<br>

#### 2. JAR 설치 및 실행

* JDK 설치

  ```shell
  # JDK 설치
  sudo apt-get install openjdk-11-jre-headless
  ```

* 각 파일 받은 후 application-prod.yml 세부 내용 수정 ([파일 링크](https://drive.google.com/drive/folders/1Cxd2qYcyGWRgLwyrqn-nSKp0ay-v7V0V?usp=sharing))

    ```shell
    # 파일 접근
    vi application-prod.yml
    ```

    ```yaml
    # 설정파일 비밀번호 수정 후 저장
    spring:
    	datasource:
    		url: jdbc:mariadb://127.0.0.1:3306/cardgame
    		username: root
    		password: <DB 비밀번호>
    		pool-size: 30
    ```

* jar파일 실행

    ```shell
    # 배포파일 실행
    java -Dspring.profiles.active=prod -jar *.jar
    ```

<br>

#### 3. 브라우저 접속 (추천 브라우저 : 크롬)

* 브라우저로 127.0.0.1:8080 접속하여 사용

<br>

#### 4. 활용법

* play video

<iframe
    width="640"
    height="480"
    src="https://youtube.com/embed/8W7sI1XssTI"
    frameborder="0"
    allow="autoplay; encrypted-media"
    allowfullscreen
>
</iframe>
