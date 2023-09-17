docker run -it  --rm -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=projectdb -v ${PWD}/db:/var/lib/mysql --net db-network --name db mysql:8
./gradlew clean bootJar
docker build -t catalog-service .
docker run -d --name catalog-service --net db-network -p 9001:9001 -e SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/projectdb  -e SPRING_PROFILES_ACTIVE=local  catalog-service
