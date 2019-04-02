/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50536
Source Host           : localhost:3306
Source Database       : health_butler

Target Server Type    : MYSQL
Target Server Version : 50536
File Encoding         : 65001

Date: 2019-04-02 11:25:01
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ai_skin
-- ----------------------------
DROP TABLE IF EXISTS `ai_skin`;
CREATE TABLE `ai_skin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) DEFAULT NULL COMMENT '用户id',
  `img_url` varchar(255) DEFAULT NULL,
  `face_quality` double DEFAULT NULL COMMENT '人脸质量评分',
  `face_token` varchar(128) DEFAULT NULL COMMENT 'face_token',
  `beauty_f` double DEFAULT '0' COMMENT '男性认为的颜值评分',
  `beauty_m` double DEFAULT '0' COMMENT '女性认为的颜值评分',
  `gender` tinyint(4) DEFAULT NULL COMMENT '性别',
  `skin_status` tinyint(4) DEFAULT NULL COMMENT '皮肤状况',
  `create_time` bigint(20) DEFAULT NULL,
  `modified_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ai测肤记录表';

-- ----------------------------
-- Table structure for ai_skin_copy
-- ----------------------------
DROP TABLE IF EXISTS `ai_skin_copy`;
CREATE TABLE `ai_skin_copy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) DEFAULT NULL COMMENT '用户id',
  `img_url` varchar(255) DEFAULT NULL,
  `face_quality` double DEFAULT NULL COMMENT '人脸质量评分',
  `face_token` varchar(128) DEFAULT NULL COMMENT 'face_token',
  `beauty_f` double DEFAULT '0' COMMENT '男性认为的颜值评分',
  `beauty_m` double DEFAULT '0' COMMENT '女性认为的颜值评分',
  `gender` tinyint(4) DEFAULT NULL COMMENT '性别',
  `skin_status` tinyint(4) DEFAULT NULL COMMENT '皮肤状况',
  `create_time` bigint(20) DEFAULT NULL,
  `modified_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ai测肤记录表';

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '资讯id',
  `source` tinyint(4) NOT NULL COMMENT '文章来源',
  `source_id` varchar(32) NOT NULL COMMENT '第三方id',
  `author` varchar(32) DEFAULT NULL COMMENT '作者',
  `title` varchar(128) DEFAULT NULL COMMENT '标题',
  `img_url` varchar(256) NOT NULL COMMENT '文章缩略图',
  `keywords` varchar(64) DEFAULT NULL COMMENT '关键字',
  `cid` bigint(20) DEFAULT NULL COMMENT '类别',
  `see` int(11) DEFAULT NULL COMMENT '观看人数',
  `content` longtext COMMENT '文章正文内容 (html格式)',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  `modified_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康资讯';

-- ----------------------------
-- Table structure for article_category
-- ----------------------------
DROP TABLE IF EXISTS `article_category`;
CREATE TABLE `article_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `first_id` bigint(20) NOT NULL COMMENT '第一类别id',
  `first_classify` varchar(32) NOT NULL COMMENT '第一类别名称',
  `second_id` bigint(20) NOT NULL COMMENT '第二类别id',
  `name` varchar(32) NOT NULL COMMENT '第二类别名称',
  `create_time` bigint(20) DEFAULT NULL,
  `modified_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章分类表';

-- ----------------------------
-- Table structure for bmi_record
-- ----------------------------
DROP TABLE IF EXISTS `bmi_record`;
CREATE TABLE `bmi_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) NOT NULL COMMENT '用户id',
  `height` double DEFAULT NULL COMMENT '身高 cm',
  `weight` double DEFAULT NULL COMMENT '体重 kg',
  `bmi` double DEFAULT NULL COMMENT 'BMI指数',
  `create_time` bigint(20) DEFAULT NULL,
  `modified_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='bmi指数';

-- ----------------------------
-- Table structure for coin_detail
-- ----------------------------
DROP TABLE IF EXISTS `coin_detail`;
CREATE TABLE `coin_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '交易id',
  `uid` bigint(20) DEFAULT NULL COMMENT '用户id',
  `type` tinyint(4) DEFAULT NULL COMMENT '交易类型',
  `toUid` bigint(20) DEFAULT NULL COMMENT '交易对象',
  `coin` bigint(20) DEFAULT NULL COMMENT '交易金额',
  `description` varchar(128) DEFAULT NULL COMMENT '交易描述',
  `create_time` bigint(20) DEFAULT NULL,
  `modified_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_uid` (`uid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康币交易明细表';

-- ----------------------------
-- Table structure for community
-- ----------------------------
DROP TABLE IF EXISTS `community`;
CREATE TABLE `community` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '社区分享id',
  `uid` bigint(20) NOT NULL COMMENT '用户id',
  `content` varchar(512) DEFAULT NULL COMMENT '发表内容',
  `img_url_list` varchar(2048) DEFAULT '' COMMENT '发表图片url list',
  `is_delete` tinyint(1) NOT NULL COMMENT '逻辑删除',
  `praise` int(11) DEFAULT NULL COMMENT '点赞数',
  `reward` int(11) DEFAULT NULL COMMENT '打赏金额总数',
  `only_me` tinyint(1) DEFAULT NULL COMMENT '是否仅自己可见, 1-是, 0-不是',
  `create_time` bigint(20) NOT NULL COMMENT '发表时间',
  `modified_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_uid` (`uid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区分享表';

-- ----------------------------
-- Table structure for community_record
-- ----------------------------
DROP TABLE IF EXISTS `community_record`;
CREATE TABLE `community_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cid` bigint(20) NOT NULL COMMENT '分享id',
  `f_uid` bigint(20) DEFAULT NULL COMMENT '操作的用户id',
  `to_uid` bigint(20) DEFAULT NULL COMMENT '被操作的用户id',
  `category` tinyint(4) DEFAULT NULL COMMENT '分类',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `content` varchar(128) DEFAULT NULL COMMENT '内容',
  `create_time` bigint(20) DEFAULT NULL COMMENT '操作时间',
  `modified_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_cid` (`cid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区点赞打赏记录表';

-- ----------------------------
-- Table structure for food
-- ----------------------------
DROP TABLE IF EXISTS `food`;
CREATE TABLE `food` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '食物id',
  `source` tinyint(4) NOT NULL COMMENT '数据来源平台',
  `source_id` varchar(32) NOT NULL COMMENT '第三方id',
  `fcid` bigint(20) NOT NULL COMMENT '分类id',
  `name` varchar(32) DEFAULT NULL COMMENT '食物名',
  `img_url` varchar(255) NOT NULL COMMENT '食物图片',
  `brief` varchar(128) DEFAULT NULL COMMENT '功能',
  `affect` int(11) DEFAULT NULL COMMENT '功效个数',
  `nutrition` int(11) DEFAULT NULL COMMENT '营养个数',
  `avoid_num` int(11) DEFAULT NULL COMMENT '相克个数',
  `suitable_num` int(11) DEFAULT NULL COMMENT '宜搭个数',
  `create_time` bigint(20) NOT NULL,
  `modified_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_fcid` (`fcid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食物表';

-- ----------------------------
-- Table structure for food_category
-- ----------------------------
DROP TABLE IF EXISTS `food_category`;
CREATE TABLE `food_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '食物分类id',
  `name` varchar(32) NOT NULL COMMENT '食物分类名',
  `img_url` varchar(255) DEFAULT NULL COMMENT '食物分类图片',
  `create_time` bigint(20) DEFAULT NULL,
  `modified_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食物分类表';

-- ----------------------------
-- Table structure for food_detail
-- ----------------------------
DROP TABLE IF EXISTS `food_detail`;
CREATE TABLE `food_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '食物详请id',
  `fid` bigint(20) DEFAULT NULL COMMENT '食物id',
  `alias_name` varchar(128) DEFAULT NULL COMMENT '别名',
  `affect` varchar(2056) DEFAULT NULL COMMENT '功效',
  `nutrition` varchar(2056) DEFAULT NULL COMMENT '营养',
  `avoid` varchar(256) DEFAULT NULL COMMENT '禁忌人群',
  `suitable` varchar(256) DEFAULT NULL COMMENT '适宜人群',
  `foods_suit` text COMMENT '宜搭食物',
  `foods_avoid` text COMMENT '相克食物',
  `create_time` bigint(20) DEFAULT NULL,
  `modified_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_fid` (`fid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食物详请表';

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `name` varchar(64) NOT NULL COMMENT '商品名',
  `price` bigint(20) NOT NULL COMMENT '所需健康币',
  `img_url` varchar(255) DEFAULT NULL COMMENT '商品图片url',
  `detail` varchar(255) DEFAULT NULL COMMENT '商品详情',
  `surplus` int(11) DEFAULT NULL COMMENT '剩余数',
  `create_time` bigint(20) NOT NULL COMMENT '加入时间',
  `modified_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- ----------------------------
-- Table structure for info_record
-- ----------------------------
DROP TABLE IF EXISTS `info_record`;
CREATE TABLE `info_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `uid` bigint(20) NOT NULL,
  `type` tinyint(4) DEFAULT NULL COMMENT '信息类型',
  `read_status` tinyint(1) DEFAULT '0' COMMENT '是否已读',
  `mid` bigint(20) DEFAULT NULL COMMENT '消息对应的id',
  `create_time` bigint(20) DEFAULT NULL,
  `modified_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户消息记录表';

-- ----------------------------
-- Table structure for operation
-- ----------------------------
DROP TABLE IF EXISTS `operation`;
CREATE TABLE `operation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(510) DEFAULT NULL COMMENT '操作描述',
  `img_list` varchar(1024) DEFAULT '' COMMENT '图片list',
  `create_time` bigint(20) DEFAULT NULL,
  `modified_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录id',
  `uid` bigint(20) NOT NULL COMMENT '用户id',
  `type` tinyint(4) NOT NULL COMMENT '记录类型, (早睡早起运动打卡, 完成任务, 兑换, 步数)',
  `extra` varchar(255) DEFAULT NULL COMMENT '扩展字段，各种记录类型可共用',
  `create_time` bigint(20) NOT NULL COMMENT '记录时间',
  `modified_time` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_uid` (`uid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户记录表';

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `reward` bigint(20) NOT NULL COMMENT '赏金',
  `category1` tinyint(4) NOT NULL COMMENT '分类1',
  `category2` tinyint(4) NOT NULL COMMENT '分类2',
  `title` varchar(32) DEFAULT NULL COMMENT '任务标题',
  `img_url` varchar(255) DEFAULT NULL COMMENT '任务图片',
  `description` varchar(128) DEFAULT NULL COMMENT '任务描述',
  `max_count` int(11) DEFAULT NULL COMMENT '最大完成次数',
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
  `open_id` varchar(32) NOT NULL COMMENT 'open_id用于标记小程序用户',
  `nick_name` varchar(64) DEFAULT NULL COMMENT '昵称',
  `gender` tinyint(2) DEFAULT NULL COMMENT '性别, 0-未知, 1-男, 2-女',
  `city` varchar(16) DEFAULT NULL COMMENT '城市',
  `province` varchar(16) DEFAULT NULL COMMENT '省',
  `country` varchar(16) DEFAULT NULL COMMENT '国家',
  `avatar_url` varchar(255) DEFAULT NULL COMMENT '头像url',
  `health_coin` bigint(20) DEFAULT '0' COMMENT '健康币',
  `reward` bigint(20) DEFAULT '0' COMMENT '赏金',
  `morning_time` varchar(32) DEFAULT NULL COMMENT '早起提醒时间, 后缀为-remind即为提醒, cancel即为取消',
  `night_time` varchar(32) DEFAULT NULL COMMENT '早睡提醒时间',
  `sport_time` varchar(32) DEFAULT NULL COMMENT '运动提醒时间',
  `sum_step` int(11) DEFAULT NULL COMMENT '总步数',
  `now_step` int(11) DEFAULT '0' COMMENT '当天步数',
  `cover_img_url` varchar(2550) DEFAULT '' COMMENT '封面图片url',
  `create_time` bigint(20) NOT NULL COMMENT '创建时间',
  `modified_time` bigint(20) NOT NULL COMMENT '最后一次修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_open_id` (`open_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
