-- 视频表
CREATE TABLE `video` (
  `id` int NOT NULL AUTO_INCREMENT, -- 主键id
  `length` varchar(50) DEFAULT NULL,  -- 视频时长
  `url` varchar(100) DEFAULT NULL,    -- 视频在minio中存储路径
  `cover` varchar(100) DEFAULT NULL,   -- 视频封面在minio中存储路径
  `name` varchar(200) DEFAULT NULL,   -- 视频名称
  `intro` varchar(200) DEFAULT NULL,  -- 视频介绍
  `user_id` int DEFAULT NULL,    -- 作者id
  `create_time` datetime DEFAULT NULL,  -- 创建时间
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 视频数据表
CREATE TABLE `video_data` (
  `id` int NOT NULL AUTO_INCREMENT,
  `video_id` int DEFAULT NULL,  -- 视频id
  `comment_count` int DEFAULT NULL,  -- 该视频评论数
  `play_count` int DEFAULT NULL,   -- 该视频播放量
  `like_count` int DEFAULT NULL,  -- 点赞量
  `danmaku_count` int DEFAULT NULL,  -- 弹幕量
  `collect_count` int DEFAULT NULL,  -- 收藏量
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 视频、评论点赞表
CREATE TABLE `likes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `video_id` int DEFAULT NULL,  -- 点赞所在的视频id
  `user_id` int DEFAULT NULL,  -- 点赞者用户id
  `comment_id` int DEFAULT NULL,  -- 点赞的评论id，若点赞的是视频则该值为null
  `create_time` datetime DEFAULT NULL,  -- 点赞创建时间
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 评论表
CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `video_id` int DEFAULT NULL,  -- 评论所在的视频id
  `user_id` int DEFAULT NULL,  -- 发表评论的用户id
  `parent_id` int DEFAULT NULL,  -- 父级评论id
  `top_id` int DEFAULT NULL,  -- 顶层评论id
  `content` varchar(1000) DEFAULT NULL,  -- 评论内容
  `create_time` datetime DEFAULT NULL,  -- 评论创建时间
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 弹幕表
CREATE TABLE `danmaku` (
  `id` int NOT NULL AUTO_INCREMENT,
  `video_id` int DEFAULT NULL,  -- 所在视频id
  `user_id` int DEFAULT NULL,  -- 发表弹幕的用户id
  `content` varchar(1000) DEFAULT NULL,  -- 弹幕内容
  `create_time` datetime DEFAULT NULL,  -- 弹幕创建时间
  `place` int DEFAULT NULL,  -- 弹幕所处视频中的位置
  `type` int DEFAULT NULL,  -- 弹幕显现方式，由左至右还是直接显现
  `color` int DEFAULT NULL,  -- 弹幕颜色
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 评论消息表
CREATE TABLE `comment_notice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sender_id` int DEFAULT NULL,  -- 评论消息的发送者id
  `receiver_id` int DEFAULT NULL,  -- 评论消息的接收者id
  `create_time` datetime DEFAULT NULL,  -- 评论消息的创建时间
  `video_id` int DEFAULT NULL,  -- 评论所在的视频id
  `status` int DEFAULT NULL,  -- 消息已读状态
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 收藏表
CREATE TABLE `collect` (
  `id` int NOT NULL AUTO_INCREMENT,
  `video_id` int DEFAULT NULL,  -- 收藏的视频id
  `collect_group_id` int DEFAULT NULL,  -- 所属收藏夹id
  `create_time` datetime DEFAULT NULL,  -- 收藏的创建时间
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 收藏夹表
CREATE TABLE `collect_group` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,  -- 收藏夹名称
  `user_id` int DEFAULT NULL,  -- 收藏夹所属用户id
  `create_time` datetime DEFAULT NULL,  -- 收藏夹创建时间
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 历史播放表
CREATE TABLE `play` (
  `id` int NOT NULL AUTO_INCREMENT,
  `video_id` int DEFAULT NULL,  -- 播放的视频id
  `user_id` int DEFAULT NULL,  -- 播放视频的用户id
  `create_time` datetime DEFAULT NULL,  -- 播放的创建时间
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=59 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 用户表
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,  -- 用户名
  `password` varchar(100) DEFAULT NULL,  -- 密码
  `cover` varchar(100) DEFAULT NULL,  -- 头像
  `nickname` varchar(100) DEFAULT NULL,  -- 昵称
  `intro` varchar(200) DEFAULT NULL,  -- 简介
  `phone_number` varchar(50) DEFAULT NULL,  -- 手机号
  `mail_number` varchar(50) DEFAULT NULL,  -- 邮箱
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 用户权限表
CREATE TABLE `privilege` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,  -- 用户id
  `collect_group` int DEFAULT NULL,  -- 收藏夹是否开放
  `remotely_like` int DEFAULT NULL,  -- 最近点赞是否开放
  `fans_list` int DEFAULT NULL,  -- 粉丝列表
  `idol_list` int DEFAULT NULL,  -- 偶像列表
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 关注表
CREATE TABLE `follow` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fans_id` int DEFAULT NULL,  -- 粉丝id
  `idol_id` int DEFAULT NULL,  -- 被关注者id
  `create_time` datetime DEFAULT NULL,  -- 关注的创建时间
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 视频合集表 （该表暂时无用）
CREATE TABLE `video_ensemble` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `intro` varchar(150) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 加视频入合集表（该表暂时无用）
CREATE TABLE `add_to_ensemble` (
  `id` int NOT NULL AUTO_INCREMENT,
  `video_id` int DEFAULT NULL,
  `ensemble_id` int DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 点赞消息表
CREATE TABLE `like_notice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sender_id` int DEFAULT NULL,  -- 点赞发起人的id
  `comment_id` int DEFAULT NULL,  -- 点赞的评论id，若点赞的是视频则为null
  `create_time` datetime DEFAULT NULL,  -- 点赞消息的生成时间
  `video_id` int DEFAULT NULL,  -- 点赞消息所在的视频id
  `status` int DEFAULT NULL,  -- 点赞消息的已读状态
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 视频动态表
CREATE TABLE `dynamic` (
  `id` int NOT NULL AUTO_INCREMENT,
  `video_cover` varchar(100) DEFAULT NULL, -- 动态视频封面
  `video_name` varchar(100) DEFAULT NULL,  -- 动态视频名称
  `create_time` datetime DEFAULT NULL,   -- 动态的创建时间
  `video_id` int DEFAULT NULL,    -- 动态的视频id
  `author_id` int DEFAULT NULL,    -- 动态的作者id
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=138 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 推送给用户视频动态表
CREATE TABLE `dynamic_to_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,  -- 需要被推送动态的用户id
  `dynamic_id` int DEFAULT NULL,  -- 需要被推送的动态id
  `status` int DEFAULT NULL,  -- 推送的动态已读状态
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=126 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 聊天消息表
CREATE TABLE `chat_notice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `sender_id` int DEFAULT NULL,  -- 私聊发起者id
  `content` varchar(500) DEFAULT NULL,  -- 私聊内容
  `receiver_id` int DEFAULT NULL, -- 私聊接收者id
  `create_time` datetime DEFAULT NULL,  -- 私聊创建时间
  `status` int DEFAULT NULL,  -- 私聊状态
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
-- 聊天会话表
CREATE TABLE `chat_session` (
 `id` int NOT NULL AUTO_INCREMENT,
  `sender_id` int DEFAULT NULL,  -- 会话发起者id
  `receiver_id` int DEFAULT NULL,  -- 会话接收者id
  `create_time` datetime DEFAULT NULL,   -- 会话创建时间
  `update_time` datetime DEFAULT NULL,  -- 会话更新时间
  `update_content` varchar(500) DEFAULT NULL,   -- 会话最近更新的内容
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
    -- 创建新增播放数据触发器，一旦在播放表插入记录则将对应视频的播放量加一  注意下方xxx代表springboot配置文件中连接数据库时实际所用用户名
CREATE DEFINER=`xxx`@`%` TRIGGER `increment_play_count` AFTER INSERT ON `play` FOR EACH ROW BEGIN
    UPDATE video_data
    SET play_count = play_count  + 1
    WHERE video_id = NEW.video_id;
END;
    -- 创建新增评论数据触发器，一旦新增评论则对应视频的评论量加一
CREATE DEFINER=`xxx`@`%` TRIGGER `increment_comment_count` AFTER INSERT ON `comment` FOR EACH ROW BEGIN
    UPDATE video_data
    SET comment_count = comment_count  + 1
    WHERE video_id = NEW.video_id;
END;
  -- 创建新增点赞数据触发器，一旦新增点赞则对应视频的点赞量加一
CREATE DEFINER=`xxx`@`%` TRIGGER `increment_like_count` AFTER INSERT ON `likes` FOR EACH ROW BEGIN
    UPDATE video_data
    SET like_count = like_count  + 1
    WHERE video_id = NEW.video_id;
END;
  -- 创建新增收藏数据触发器，一旦新增收藏则对应视频的收藏量加一
CREATE DEFINER=`xxx`@`%` TRIGGER `increment_collect_count` AFTER INSERT ON `collect` FOR EACH ROW BEGIN
    UPDATE video_data
    SET collect_count  = collect_count  + 1
    WHERE video_id = NEW.video_id;
END;
  -- 创建减少收藏数据触发器，一旦取消收藏则对应视频的收藏量减一
CREATE DEFINER=`xxx`@`%` TRIGGER `de_collect_count` AFTER DELETE ON `collect` FOR EACH ROW BEGIN
    UPDATE video_data
    SET collect_count = GREATEST(collect_count - 1, 0)
    WHERE video_id = OLD.video_id;
END;
  -- 创建减少点赞数据触发器，一旦取消点赞则对应视频的点赞量减一
CREATE DEFINER=`xxx`@`%` TRIGGER `de_like_count` AFTER DELETE ON `likes` FOR EACH ROW BEGIN
    UPDATE video_data
    SET like_count  = GREATEST(like_count  - 1, 0)
    WHERE video_id = OLD.video_id;
END;
  -- 创建新增弹幕数据触发器，一旦新增弹幕则对应视频的弹幕量加一
CREATE DEFINER=`xxx`@`%` TRIGGER `increment_danmaku_count` AFTER INSERT ON `danmaku` FOR EACH ROW BEGIN
    UPDATE video_data
    SET danmaku_count = danmaku_count  + 1
    WHERE video_id = NEW.video_id;
END;








































































































































































CREATE TABLE IF NOT EXISTS zipkin_spans (
  `trace_id_high` BIGINT NOT NULL DEFAULT 0 COMMENT 'If non zero, this means the trace uses 128 bit traceIds instead of 64 bit',
  `trace_id` BIGINT NOT NULL,
  `id` BIGINT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `remote_service_name` VARCHAR(255),
  `parent_id` BIGINT,
  `debug` BIT(1),
  `start_ts` BIGINT COMMENT 'Span.timestamp(): epoch micros used for endTs query and to implement TTL',
  `duration` BIGINT COMMENT 'Span.duration(): micros used for minDuration and maxDuration query',
  PRIMARY KEY (`trace_id_high`, `trace_id`, `id`)
);
 
ALTER TABLE zipkin_spans ADD INDEX(`trace_id_high`, `trace_id`) COMMENT 'for getTracesByIds';
ALTER TABLE zipkin_spans ADD INDEX(`name`) COMMENT 'for getTraces and getSpanNames';
ALTER TABLE zipkin_spans ADD INDEX(`remote_service_name`) COMMENT 'for getTraces and getRemoteServiceNames';
ALTER TABLE zipkin_spans ADD INDEX(`start_ts`) COMMENT 'for getTraces ordering and range';
 
CREATE TABLE IF NOT EXISTS zipkin_annotations (
  `trace_id_high` BIGINT NOT NULL DEFAULT 0 COMMENT 'If non zero, this means the trace uses 128 bit traceIds instead of 64 bit',
  `trace_id` BIGINT NOT NULL COMMENT 'coincides with zipkin_spans.trace_id',
  `span_id` BIGINT NOT NULL COMMENT 'coincides with zipkin_spans.id',
  `a_key` VARCHAR(255) NOT NULL COMMENT 'BinaryAnnotation.key or Annotation.value if type == -1',
  `a_value` BLOB COMMENT 'BinaryAnnotation.value(), which must be smaller than 64KB',
  `a_type` INT NOT NULL COMMENT 'BinaryAnnotation.type() or -1 if Annotation',
  `a_timestamp` BIGINT COMMENT 'Used to implement TTL; Annotation.timestamp or zipkin_spans.timestamp',
  `endpoint_ipv4` INT COMMENT 'Null when Binary/Annotation.endpoint is null',
  `endpoint_ipv6` BINARY(16) COMMENT 'Null when Binary/Annotation.endpoint is null, or no IPv6 address',
  `endpoint_port` SMALLINT COMMENT 'Null when Binary/Annotation.endpoint is null',
  `endpoint_service_name` VARCHAR(255) COMMENT 'Null when Binary/Annotation.endpoint is null'
) ;
 
ALTER TABLE zipkin_annotations ADD UNIQUE KEY(`trace_id_high`, `trace_id`, `span_id`, `a_key`, `a_timestamp`) COMMENT 'Ignore insert on duplicate';
ALTER TABLE zipkin_annotations ADD INDEX(`trace_id_high`, `trace_id`, `span_id`) COMMENT 'for joining with zipkin_spans';
ALTER TABLE zipkin_annotations ADD INDEX(`trace_id_high`, `trace_id`) COMMENT 'for getTraces/ByIds';
ALTER TABLE zipkin_annotations ADD INDEX(`endpoint_service_name`) COMMENT 'for getTraces and getServiceNames';
ALTER TABLE zipkin_annotations ADD INDEX(`a_type`) COMMENT 'for getTraces and autocomplete values';
ALTER TABLE zipkin_annotations ADD INDEX(`a_key`) COMMENT 'for getTraces and autocomplete values';
ALTER TABLE zipkin_annotations ADD INDEX(`trace_id`, `span_id`, `a_key`) COMMENT 'for dependencies job';
 
CREATE TABLE IF NOT EXISTS zipkin_dependencies (
  `day` DATE NOT NULL,
  `parent` VARCHAR(255) NOT NULL,
  `child` VARCHAR(255) NOT NULL,
  `call_count` BIGINT,
  `error_count` BIGINT,
  PRIMARY KEY (`day`, `parent`, `child`)
  
);
