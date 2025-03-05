CREATE TABLE review(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  comment TEXT,
  rating INT,
  coffe_id BIGINT,
  FOREIGN KEY (coffe_id) REFERENCES coffee(id)
);