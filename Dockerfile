FROM openjdk:17
COPY target/wallet-0.0.1-SNAPSHOT.jar /home/wallet.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/wallet.jar"]