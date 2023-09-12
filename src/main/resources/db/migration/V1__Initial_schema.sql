CREATE TABLE books (
id                  BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
author              varchar(255) NOT NULL,
isbn                varchar(255) UNIQUE NOT NULL,
price               float8 NOT NULL,
title               varchar(255) NOT NULL,
version             integer NOT NULL,
created_date        DATETIME,
last_modified_date  DATETIME
);