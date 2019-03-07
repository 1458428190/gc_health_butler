/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50536
Source Host           : localhost:3306
Source Database       : health_butler

Target Server Type    : MYSQL
Target Server Version : 50536
File Encoding         : 65001

Date: 2019-01-13 12:51:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `sid` bigint(20) NOT NULL COMMENT '分享id',
  `uid` bigint(20) NOT NULL COMMENT '用户id',
  `content` varchar(128) DEFAULT NULL COMMENT '评论内容',
  `create_time` bigint(20) NOT NULL COMMENT '评论时间',
  `modified_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_cid` (`sid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- ----------------------------
-- Table structure for food
-- ----------------------------
DROP TABLE IF EXISTS `food`;
CREATE TABLE `food` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '食物id',
  `food_name` varchar(64) NOT NULL COMMENT '食物名',
  `type` tinyint(4) NOT NULL COMMENT '食物类型',
  `nutrient_content` varchar(255) NOT NULL COMMENT '营养成分',
  `description` varchar(1024) DEFAULT NULL COMMENT '描述',
  `calorie` int(11) NOT NULL COMMENT '卡路里',
  `create_time` bigint(20) NOT NULL,
  `modified_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食物表';

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `goods_name` varchar(64) NOT NULL COMMENT '商品名',
  `original_price` bigint(20) NOT NULL COMMENT '原价（分）',
  `health_coin_price` bigint(20) NOT NULL COMMENT '所需健康币',
  `image_url` varchar(64) DEFAULT NULL COMMENT '商品图片url',
  `detail` varchar(255) DEFAULT NULL COMMENT '商品详情',
  `create_time` bigint(20) NOT NULL COMMENT '加入时间',
  `modified_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- ----------------------------
-- Table structure for health_coin_detail
-- ----------------------------
DROP TABLE IF EXISTS `health_coin_detail`;
CREATE TABLE `health_coin_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '交易id',
  `uid` bigint(20) NOT NULL COMMENT '用户id',
  `health_coin` bigint(20) DEFAULT NULL COMMENT '交易的健康币数量',
  `o_uid` bigint(20) NOT NULL COMMENT '对方id',
  `type` tinyint(4) NOT NULL COMMENT '交易类型',
  `description` varchar(128) NOT NULL COMMENT '交易描述',
  `create_time` bigint(20) NOT NULL COMMENT '交易时间',
  `modified_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_ui` (`uid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康币交易明细表';

-- ----------------------------
-- Table structure for health_info
-- ----------------------------
DROP TABLE IF EXISTS `health_info`;
CREATE TABLE `health_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资讯id',
  `type` tinyint(4) NOT NULL COMMENT '文章类型',
  `title` varchar(64) NOT NULL COMMENT '标题',
  `abstract` varchar(128) DEFAULT NULL COMMENT '摘要',
  `content_url` varchar(128) NOT NULL COMMENT '文章url',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  `modified_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康资讯';

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录id',
  `uid` bigint(20) NOT NULL COMMENT '用户id',
  `type` tinyint(4) NOT NULL COMMENT '记录类型,（身体指数，步数，运动，早睡，兑换，完成任务，点赞）',
  `extra` varchar(255) DEFAULT NULL COMMENT '扩展字段，各种记录类型可共用',
  `create_time` bigint(20) NOT NULL COMMENT '记录时间',
  `modified_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_uid` (`uid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='记录表';

-- ----------------------------
-- Table structure for share_community
-- ----------------------------
DROP TABLE IF EXISTS `share_community`;
CREATE TABLE `share_community` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '社区分享id',
  `uid` bigint(20) NOT NULL COMMENT '用户id',
  `content` varchar(255) DEFAULT NULL COMMENT '发表内容',
  `image_list` varchar(1024) NOT NULL COMMENT '发表图片url list',
  `is_delete` tinyint(1) NOT NULL COMMENT '逻辑删除',
  `likenum_int` int(11) DEFAULT NULL COMMENT '点赞数',
  `comment_num` int(11) DEFAULT NULL COMMENT '评论数',
  `create_time` bigint(20) NOT NULL COMMENT '发表时间',
  `modified_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_uid` (`uid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分享表';

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `reward` bigint(20) NOT NULL COMMENT '赏金',
  `type` tinyint(4) NOT NULL COMMENT '任务类型',
  `content` varchar(255) NOT NULL COMMENT '任务内容',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  `modified_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `wx` varchar(64) NOT NULL COMMENT '微信号',
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `image` varchar(128) DEFAULT NULL COMMENT '用户头像地址',
  `total_step` bigint(20) DEFAULT NULL COMMENT '历史总步数',
  `health_coin` bigint(20) DEFAULT NULL COMMENT '健康币',
  `sleep_time` varchar(128) DEFAULT NULL COMMENT '睡觉提醒时间,可设置多个时间',
  `sport_time` varchar(128) DEFAULT NULL COMMENT '运动提醒时间，可设置多个时间',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  `modified_time` bigint(20) NOT NULL COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
