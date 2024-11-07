DROP TABLE IF EXISTS `incident`;

CREATE TABLE `incident`
(
    id BIGINT NOT NULL COMMENT 'incident id',
    description text NULL DEFAULT NULL COMMENT 'incident description',
    status VARCHAR(32) NULL DEFAULT NULL COMMENT 'incident status',
    PRIMARY KEY (id)
);