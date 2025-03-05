CREATE TABLE coffee_tag(
  coffee_id BIGINT,
  tag_id BIGINT,
  PRIMARY KEY (coffee_id,tag_id),
  FOREIGN KEY (coffee_id) REFERENCES coffee(id),
  FOREIGN KEY (tag_id) REFERENCES tag(id)
);