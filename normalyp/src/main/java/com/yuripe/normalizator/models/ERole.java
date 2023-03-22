package com.yuripe.normalizator.models;

/*
INSERT INTO `carmanagement`.`roles` (`role_id`, `name`) VALUES ('1', 'ROLE_USER');
INSERT INTO `carmanagement`.`roles` (`role_id`, `name`) VALUES ('2', 'ROLE_SUPERVISOR');
INSERT INTO `carmanagement`.`roles` (`role_id`, `name`) VALUES ('3', 'ROLE_ADMIN');
 */
public enum ERole {
  ROLE_USER,
  ROLE_SUPERVISOR,
  ROLE_ADMIN
}
