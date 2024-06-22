package ljl.bilibili.mapper.video.video_production.upload;
import com.github.yulichang.base.MPJBaseMapper;
import ljl.bilibili.entity.video.video_production.upload.VideoData;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface VideoDataMapper extends MPJBaseMapper<VideoData> {



    // 构造函数、getters和setters可以由@Data注解自动生成
}
