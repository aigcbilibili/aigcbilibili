package ljl.bilibili.search.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ljl.bilibili.client.pojo.RecommendVideo;
import ljl.bilibili.search.service.SearchService;
import ljl.bilibili.search.vo.request.EsKeywordRequest;
import ljl.bilibili.search.vo.response.UserKeyWordSearchResponse;
import ljl.bilibili.search.handler.MysqlToEsHandler;
import ljl.bilibili.search.vo.response.TotalCountSearchResponse;
import ljl.bilibili.search.vo.response.VideoKeywordSearchResponse;
import ljl.bilibili.search.vo.request.EsIndexRequest;
import ljl.bilibili.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/search")
@Api(tags = "搜索")
@Slf4j
@CrossOrigin(value = "*")
public class SearchController {
    @Resource
    SearchService searchService;
    @Resource
    MysqlToEsHandler mysqlToEsHandler;
    @GetMapping("/test")
    public Boolean test() throws Exception {
        mysqlToEsHandler.mysqlToEsHandler();
        return true;
    }
   
    @GetMapping("/totalKeywordSearch/{keyword}")
    @ApiOperation("关键字搜索后得出匹配视频和用户的总页数和总文档数")
    public Result<TotalCountSearchResponse> totalKeywordSearch(@PathVariable String keyword) {
        log.info("1");
        return searchService.totalKeywordSearch(keyword);
    }
   
    @GetMapping("/videoKeywordSearch/{keyword}/{pageNumber}/{type}")
    @ApiOperation("关键字搜索后得出匹配度降序排列的视频列表，得到对应页码的视频数据")
    public Result<List<VideoKeywordSearchResponse>> videoPageKeywordSearch(@PathVariable String keyword, @PathVariable Integer pageNumber, @PathVariable Integer type) throws JsonProcessingException {
        log.info("1");
        return searchService.videoPageKeywordSearch(keyword, pageNumber, type);
    }
   
    @GetMapping("/userKeywordSearch/{keyword}/{pageNumber}/{type}/{userId}")
    @ApiOperation("关键字搜索后得出匹配度降序排列的用户，得到对应页码的视频数据,传不同type决定再查询结果")
    public Result<List<UserKeyWordSearchResponse>> userPageKeywordSearch(@PathVariable String keyword, @PathVariable Integer pageNumber, @PathVariable Integer type, @PathVariable Integer userId) throws JsonProcessingException {
        log.info("1");
        return searchService.userPageKeywordSearch(keyword, pageNumber, type,userId);
    }
   
    @GetMapping("/likelyKeyWordSearch/{keyword}")
    @ApiOperation("输入关键字时停顿且未点确认导致跳转搜索结果页时对应的匹配度前十的近似关键字")
    public Result<List<String>> LikelykeyWordSearch(@PathVariable String keyword) throws IOException {
        log.info("1");
        return searchService.likelyKeywordSearch(keyword);
    }
    @GetMapping("/likelyVideoRecommend/{videoId}")
    @ApiOperation("推荐视频")
    @ApiIgnore
    public List<RecommendVideo> likelyVideoRecommend(@PathVariable String videoId) throws IOException {
        log.info("1");
        return searchService.likelyVideoRecommend(videoId);
    }
   
    @PostMapping("/addKeywordSearchRecord")
    @ApiOperation("增加搜索的历史记录")
    public Result<Boolean> addKeywordSearchRecord(@RequestBody EsKeywordRequest esKeywordRequest) throws IOException {
        log.info("1");
        return searchService.addKeywordSearchRecord(esKeywordRequest);
    }
   
    @PostMapping("/createIndex")
    @ApiIgnore
    public void crateIndex(@RequestBody EsIndexRequest esIndexRequest) {
        log.info("1");
        searchService.createIndex(esIndexRequest);
    }
    @PostMapping("/deleteIndex/{indexName}")
    @ApiIgnore
    public void deleteIndex(@PathVariable String indexName) throws IOException {
        log.info("1");
        searchService.deleteIndex(indexName);
    }

}