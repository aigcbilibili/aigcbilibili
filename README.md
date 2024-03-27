## LABiliBili视频网站

<br>

### 1. 项目概述
1. 技术栈：`Vue3` `ElementPlus` `Pinia` `Vue-Router` `SaSS` `WebSocket` `Axios` `Swagger` `XXL-JOB` ` ElasticSearch` `RocketMQ` `Spring Boot` `Srping Cloud` `Spring CloudAlibaba` `Redis` `Mybatis-Plus` `Druid` `MySQL`
2. 介绍：**labilibili** 是一个参考了bilibili和youtube的视频网站，采用流行Vue3 + SpringBoot + Minio + MySQL开发，旨在提供一个前后端分离、功能丰富的视频分享平台。
已上线地址：https://labilibili.com <br/>
3. 涵盖功能：
   1. 响应式界面，兼容不同浏览器，支持PC端半屏和全屏的界面
   2. 三种登录方式（账号密码、手机号和邮箱）和注册的支持
   3. 鉴权与授权，登录态与游客态的隔离
   4. 视频的显示、点赞、收藏、弹幕、评论、项目内分享、上传
   5. 个人信息查看编辑
   6. 点赞、回复消息查看，私聊（含星火大模型）
   7. 动态、历史记录、热点等的展示
   8. 用户中心权限控制
4. 技术点：篇幅所限，在此不表

<div align=center><img src="./screenshots/首屏.png" alt="进程与线程" style="width: 800px;"/></div>

### 2. 页面展示
1. 登录页面：
    <div align=center><img src="./screenshots/登录.png" alt="进程与线程" style="width: 800px;"/></div> 
2. 搜索页面：
    <div align=center><img src="./screenshots/搜索结果页.jpg" alt="进程与线程" style="width: 800px;"/></div>

3. 详情页：
    <div align=center><img src="./screenshots/视频详情.png" alt="进程与线程" style="width: 800px;"/></div>

4. 个人主页：
    <div align=center><img src="./screenshots/个人主页-权限设置.png" alt="进程与线程" style="width: 800px;"/></div> 

5. 历史记录
    <div align=center><img src="./screenshots/历史记录.png" alt="进程与线程" style="width: 800px;"/></div> 

6. 私聊页面
     <div align=center><img src="./screenshots/大模型聊天.png" alt="进程与线程" style="width: 800px;"/></div> 

### 3. 贡献指南
项目多有不足，如果想帮助**labilibili**变得更好，请遵循以下步骤：

1. Fork 本仓库。
2. 创建你的特性分支 (`git checkout -b feature/AmazingFeature`)。
3. 提交你的更改 (`git commit -m 'Add some AmazingFeature'`)。
4. 将你的更改推送到分支 (`git push origin feature/AmazingFeature`)。
5. 打开一个Pull Request。
### 4.数据库文件
## 视频表
CREATE TABLE `video` (
  `id` int NOT NULL AUTO_INCREMENT,
  `length` varchar(50) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `cover` varchar(100) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `intro` varchar(200) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
## 视频数据表
CREATE TABLE `video_data` (
  `id` int NOT NULL AUTO_INCREMENT,
  `video_id` int DEFAULT NULL,
  `comment_count` int DEFAULT NULL,
  `play_count` int DEFAULT NULL,
  `like_count` int DEFAULT NULL,
  `danmaku_count` int DEFAULT NULL,
  `collect_count` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
## 视频、评论点赞表
CREATE TABLE `likes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `video_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `comment_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
## 评论表
CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `video_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `parent_id` int DEFAULT NULL,
  `top_id` int DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
## 弹幕表
CREATE TABLE `danmaku` (
  `id` int NOT NULL AUTO_INCREMENT,
  `video_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `place` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
## 收藏表
CREATE TABLE `collect` (
  `id` int NOT NULL AUTO_INCREMENT,
  `video_id` int DEFAULT NULL,
  `collect_group_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
## 收藏夹表
CREATE TABLE `collect_group` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
## 历史播放表
CREATE TABLE `play` (
  `id` int NOT NULL AUTO_INCREMENT,
  `video_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
## 用户表
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `cover` varchar(100) DEFAULT NULL,
  `nickname` varchar(100) DEFAULT NULL,
  `intro` varchar(200) DEFAULT NULL,
  `phone_number` varchar(50) DEFAULT NULL,
  `mail_number` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
## 用户权限表
CREATE TABLE `privilege` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `collect_group` int DEFAULT NULL,
  `remotely_like` int DEFAULT NULL,
  `fans_list` int DEFAULT NULL,
  `idol_list` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
## 关注表
CREATE TABLE `follow` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fans_id` int DEFAULT NULL,
  `idol_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
## 视频合集表
CREATE TABLE `video_ensemble` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `intro` varchar(150) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
## 加视频入合集表
CREATE TABLE `add_to_ensemble` (
  `id` int NOT NULL AUTO_INCREMENT,
  `video_id` int DEFAULT NULL,
  `ensemble_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
## 点赞消息表
CREATE TABLE `like_notice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sender_id` int DEFAULT NULL,
  `comment_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `video_id` int DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
## 评论消息表
CREATE TABLE `comment_notice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sender_id` int DEFAULT NULL,
  `receiver_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `video_id` int DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
## 视频动态表
CREATE TABLE `dynamic` (
  `id` int NOT NULL AUTO_INCREMENT,
  `video_cover` varchar(100) DEFAULT NULL,
  `video_name` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `video_id` int DEFAULT NULL,
  `author_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=138 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
## 推送给用户视频动态表
CREATE TABLE `dynamic_to_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `dynamic_id` int DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=126 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
## 聊天消息表
CREATE TABLE `comment_notice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sender_id` int DEFAULT NULL,
  `receiver_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `video_id` int DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
## 聊天会话表
CREATE TABLE `chat_session` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sender_id` int DEFAULT NULL,
  `receiver_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_content` varchar(500) DEFAULT NULL,
  `status` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
