# SpringBootPlay
Retrofit SBShop backend with SpringBoot 1.5.10

I got started from
http://start.spring.io/


CMDs:  
mvn spring-boot:run  
mvn spring-boot:run -Drun.profiles=dev2  
java -jar -Dspring.profiles.active=dev2 xxx.jar  
mvn test -DskipTests=false,test=IntegrationTest
mvn dependency:tree -Dscope=test
mvn clean package


./gradlew tasks --all
./gradlew test -i
./gradlew bootRepackage
./gradlew bootRun -Dspring.profiles.active=dev2
