/*
Navicat MySQL Data Transfer

Source Server         : mySqlConnection
Source Server Version : 50142
Source Host           : localhost:3306
Source Database       : studentdb

Target Server Type    : MYSQL
Target Server Version : 50142
File Encoding         : 65001

Date: 2012-05-17 05:52:52
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `专业表`
-- ----------------------------
DROP TABLE IF EXISTS `专业表`;
CREATE TABLE `专业表` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `专业名称` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of 专业表
-- ----------------------------

-- ----------------------------
-- Table structure for `学生表`
-- ----------------------------
DROP TABLE IF EXISTS `学生表`;
CREATE TABLE `学生表` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `学号` char(7) NOT NULL,
  `姓名` varchar(20) DEFAULT NULL,
  `专业_id` int(11) DEFAULT NULL,
  `性别` bit(1) DEFAULT NULL,
  `密码` varchar(10) NOT NULL DEFAULT '12345',
  `图片_id` int(11) DEFAULT '-1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of 学生表
-- ----------------------------

-- ----------------------------
-- Table structure for `文件共享表`
-- ----------------------------
DROP TABLE IF EXISTS `文件共享表`;
CREATE TABLE `文件共享表` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `学生_id` int(11) NOT NULL,
  `路径` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of 文件共享表
-- ----------------------------

-- ----------------------------
-- Table structure for `消息历史`
-- ----------------------------
DROP TABLE IF EXISTS `消息历史`;
CREATE TABLE `消息历史` (
  `id` int(11) NOT NULL DEFAULT '0',
  `学生_id` int(11) DEFAULT NULL,
  `消息_id` int(11) DEFAULT NULL,
  `已读` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of 消息历史
-- ----------------------------

-- ----------------------------
-- Table structure for `消息表`
-- ----------------------------
DROP TABLE IF EXISTS `消息表`;
CREATE TABLE `消息表` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `内容` varchar(300) DEFAULT NULL,
  `时间` datetime DEFAULT NULL,
  `创建人_id` int(11) DEFAULT NULL,
  `标题` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of 消息表
-- ----------------------------
