CREATE TABLE coffee(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(120) NOT NULL ,
    count BIGINT,
    origin_details_id BIGINT UNIQUE,
    FOREIGN KEY (origin_details_id) REFERENCES origin_details(id)
);