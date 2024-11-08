DROP TABLE IF EXISTS `incident`;

CREATE TABLE `incident`
(
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'incident id',
    reporter VARCHAR(256) NULL DEFAULT NULL COMMENT 'incident reporter',
    title VARCHAR(256) NULL DEFAULT NULL COMMENT 'incident title',
    description text NULL DEFAULT NULL COMMENT 'incident description',
    status VARCHAR(32) NULL DEFAULT NULL COMMENT 'incident status',
    priority VARCHAR(32) NULL DEFAULT NULL COMMENT 'incident priority',
    created_time DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'incident created time',
    modified_time DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'incident modified time',
    PRIMARY KEY (id)
);

create INDEX idx_created_time_status_priority on incident(created_time, status, priority);