CREATE TABLE origin_details (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  country VARCHAR(256),
  region VARCHAR(256),
  farm VARCHAR(256),
  coffee_id BIGINT UNIQUE,
  FOREIGN KEY (coffee_id) REFERENCES coffee(id)
);