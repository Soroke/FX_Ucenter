/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50721
Source Host           : 127.0.0.1:3306
Source Database       : uuu

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-03-12 11:24:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `cases`
-- ----------------------------
DROP TABLE IF EXISTS `cases`;
CREATE TABLE `cases` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '方法ID',
  `sys_id` int(11) NOT NULL COMMENT '系统ID',
  `case_name` varchar(255) NOT NULL COMMENT '用例名',
  `method_name` varchar(255) NOT NULL COMMENT '方法名',
  PRIMARY KEY (`id`),
  KEY `case_id` (`sys_id`),
  CONSTRAINT `case_id` FOREIGN KEY (`sys_id`) REFERENCES `system` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cases
-- ----------------------------
INSERT INTO `cases` VALUES ('1', '1', 'net.faxuan.ucenter.UcenterTest', 'currency');

-- ----------------------------
-- Table structure for `datas`
-- ----------------------------
DROP TABLE IF EXISTS `datas`;
CREATE TABLE `datas` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '输入数据的id',
  `case_id` int(11) NOT NULL COMMENT '方法ID',
  `url` varchar(255) NOT NULL COMMENT '接口url',
  `params` varchar(255) NOT NULL COMMENT '参数',
  `precondition` varchar(255) DEFAULT NULL COMMENT '前置条件',
  `expected_results` varchar(255) NOT NULL COMMENT '预期结果',
  `actual_results` varchar(255) DEFAULT NULL COMMENT '实际结果',
  `description` varchar(255) DEFAULT NULL COMMENT '测试功能描述',
  PRIMARY KEY (`id`),
  KEY `method_id` (`case_id`),
  CONSTRAINT `method_id` FOREIGN KEY (`case_id`) REFERENCES `cases` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of datas
-- ----------------------------

-- ----------------------------
-- Table structure for `system`
-- ----------------------------
DROP TABLE IF EXISTS `system`;
CREATE TABLE `system` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(255) NOT NULL COMMENT '系统名称',
  `function` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system
-- ----------------------------
INSERT INTO `system` VALUES ('1', '用户中心', 'UCenter');
INSERT INTO `system` VALUES ('2', '销账系统', 'Sale');
