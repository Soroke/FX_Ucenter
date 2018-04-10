/*
Navicat MySQL Data Transfer

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2018-04-10 13:48:38
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cases
-- ----------------------------
INSERT INTO `cases` VALUES ('1', '1', 'net.faxuan.ucenter.UcenterTest', 'currency');
INSERT INTO `cases` VALUES ('2', '2', 'net.faxuan.sale.SalesTest', 'currency');

-- ----------------------------
-- Table structure for `datas`
-- ----------------------------
DROP TABLE IF EXISTS `datas`;
CREATE TABLE `datas` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '输入数据的id',
  `case_id` int(11) NOT NULL COMMENT '方法ID',
  `api_type` varchar(255) NOT NULL COMMENT '接口类型',
  `url` varchar(255) NOT NULL COMMENT '接口url',
  `params` varchar(10000) DEFAULT NULL COMMENT '参数',
  `precondition` varchar(255) DEFAULT NULL COMMENT '前置条件',
  `expected_results` varchar(255) DEFAULT NULL COMMENT '预期结果',
  `actual_results` varchar(255) DEFAULT NULL COMMENT '实际结果',
  `description` varchar(255) DEFAULT NULL COMMENT '测试功能描述',
  PRIMARY KEY (`id`),
  KEY `method_id` (`case_id`),
  CONSTRAINT `method_id` FOREIGN KEY (`case_id`) REFERENCES `cases` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=975 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `system`
-- ----------------------------
DROP TABLE IF EXISTS `system`;
CREATE TABLE `system` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` char(255) NOT NULL COMMENT '系统名称',
  `function` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of system
-- ----------------------------
INSERT INTO `system` VALUES ('1', '用户中心', 'UCenter');
INSERT INTO `system` VALUES ('2', '销账系统', 'Sales');
