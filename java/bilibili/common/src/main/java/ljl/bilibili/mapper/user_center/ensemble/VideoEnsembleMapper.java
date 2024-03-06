package ljl.bilibili.mapper.user_center.ensemble;
import com.github.yulichang.base.MPJBaseMapper;
import ljl.bilibili.entity.user_center.ensemble.VideoEnsemble;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VideoEnsembleMapper  extends MPJBaseMapper<VideoEnsemble> {
    @Select("<script>"
            + "SELECT SUM(play_count) "
            + "FROM video_data "
            + "WHERE video_id IN "
            + "<foreach item='id' collection='ids' open='(' separator=',' close=')'>"
            + "#{id}"
            + "</foreach>"
            + "</script>")
    long getTotalPlayCount(List<Integer> ids);

}
