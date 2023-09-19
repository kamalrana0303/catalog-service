package com.whatbook.catalogservice;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;


public class TestConstants {
    public static MySQLContainer<?> mySQLContainer= new MySQLContainer<>(DockerImageName.parse("mysql:8"))
            .withDatabaseName("projectdb")
            .withUsername("root")
            .withPassword("password")
            .waitingFor(Wait.forListeningPort())
            .withEnv("MYSQL_ROOT_HOST","%");
}
