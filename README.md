# Redis distributed lock
* redis 를 이용해서 분산환경에서의 순번 채번

## docker redis 설치
* docker image 를 내려받는다.
```
 > docker pull redis
```
* image 를 구동시키면 바로 사용가능하지만 redis-cli 를 이용하려면 2개의 컨테이너가 필요하고 network 으로 묶어야 한다.
```
 > docker network create redis-net
```
* disk volume 으로 사용할 경로를 만들고(folder) docker 를 실행시킨다.
```
 > docker run --name bigzero-redis \
	-p 6379:6379 \
	--network redis-net \
	-v folder:/data \
	-d redis redis-server --appendonly yes
```
* redis-cli 로 접속하기
```
 > docker run -it --network redis-net --rm redis redis-cli -h bigzero-redis
```

## source 구조
* Main.kt => 실행모듈
* MainThread.kt => Runnable worker

## 실행방법
### IDE 사용시
* ide 에서 Main.kt 실행
### gradle 로 excutable jar 만들어서 실행시
* ./gradlew clean build 이후 java -jar build/libs/RedisLockTest.jar 실행
