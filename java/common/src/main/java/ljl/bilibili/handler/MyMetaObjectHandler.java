package ljl.bilibili.handler;
 
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
/**
 *自动填充处理器
 */

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
 
 
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("nickname", "新用户"+ UUID.randomUUID().toString().substring(0,10), metaObject);
        this.setFieldValByName("cover","https://labilibili.com/user-cover/default.png",metaObject);
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("danmakuCount", 0, metaObject);
        this.setFieldValByName("playCount",0, metaObject);
        this.setFieldValByName("likeCount",0, metaObject);
        this.setFieldValByName("collectCount",0, metaObject);
        this.setFieldValByName("commentCount",0, metaObject);
        this.setFieldValByName("status",0,metaObject);
        this.setFieldValByName("collectGroup",0,metaObject);
        this.setFieldValByName("remotelyLike",0,metaObject);
        this.setFieldValByName("fansList",0,metaObject);
        this.setFieldValByName("idolList",0,metaObject);
    }
 
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
}