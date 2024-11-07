DROP TABLE IF EXISTS `incident`;

CREATE TABLE `incident`
(
    id BIGINT NOT NULL COMMENT 'primary key',
    description VARCHAR(30) NULL DEFAULT NULL COMMENT 'description of the incident',
    status VARCHAR(30) NULL DEFAULT NULL COMMENT 'status of the incident',
    PRIMARY KEY (id)
);