/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50721
Source Host           : 127.0.0.1:3306
Source Database       : uuu

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-03-07 17:55:13
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `cases`
-- ----------------------------
DROP TABLE IF EXISTS `cases`;
CREATE TABLE `cases` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(255) NOT NULL COMMENT '用例名称',
  `function` varchar(255) DEFAULT NULL COMMENT '测试类描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `datas`
-- ----------------------------
DROP TABLE IF EXISTS `datas`;
CREATE TABLE `datas` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '输入数据的id',
  `method_id` int(11) NOT NULL COMMENT '方法ID',
  `url` varchar(255) NOT NULL COMMENT '接口url',
  `params` varchar(255) NOT NULL COMMENT '参数',
  `precondition` varchar(255) DEFAULT NULL COMMENT '前置条件(是否需要登录)',
  `expected_results` int(11) NOT NULL COMMENT '预期结果',
  `actual_results` varchar(255) DEFAULT NULL COMMENT '实际结果',
  `description` varchar(255) DEFAULT NULL COMMENT '测试功能描述',
  PRIMARY KEY (`id`),
  KEY `method_id` (`method_id`),
  CONSTRAINT `method_id` FOREIGN KEY (`method_id`) REFERENCES `methods` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for `methods`
-- ----------------------------
DROP TABLE IF EXISTS `methods`;
CREATE TABLE `methods` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '方法ID',
  `case_id` int(11) NOT NULL COMMENT '所属用例',
  `method_name` varchar(255) NOT NULL COMMENT '方法名',
  PRIMARY KEY (`id`),
  KEY `case_id` (`case_id`),
  CONSTRAINT `case_id` FOREIGN KEY (`case_id`) REFERENCES `cases` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
