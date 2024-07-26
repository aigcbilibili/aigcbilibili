package ljl.bilibili.search.handler;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.xxl.job.core.handler.annotation.XxlJob;
import ljl.bilibili.search.constant.Constant;
import ljl.bilibili.search.service.MysqlToEsService;
import ljl.bilibili.search.service.impl.MysqlToEsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *MySQL到ES数据同步的定时任务执行器
 */
@Component
@Slf4j
public class MysqlToEsHandler {
    @Resource
    RedisTemplate objectRedisTemplate;
    @Resource
    RestHighLevelClient client;
    @Resource
    MysqlToEsService mysqlToEsService;
    private static Boolean hasSynchronousVideo = false;
    private static Boolean hasSynchronousUser = false;
    BloomFilter<Integer> videoFilter = BloomFilter.create(
            Funnels.integerFunnel(),
            1000,
            0.01);
    BloomFilter<Integer> userFilter = BloomFilter.create(
            Funnels.integerFunnel(),
            1000,
            0.01);
    /**
     *同步MySQL数据到ES
     */
    @XxlJob("mysqlToEs")
    public void mysqlToEsHandler() throws Exception {
        //查询是否已同步过视频数据，没有的话全量同步视频数据
        if (hasSynchronousVideo == false) {
            mysqlToEsService.videoMysqlToEs();
            mysqlToEsService.updateVideoData();
            hasSynchronousVideo = true;
        }
        //查询是否已同步过用户数据，没有的话全量同步用户数据
        if (hasSynchronousUser == false) {
            mysqlToEsService.userMysqlToEs();
            mysqlToEsService.updateUserData();
            hasSynchronousUser = true;
        }
        //将redis存储的视频、用户相关增删改的操作记录下来进行增量同步
        List<HashMap<String, Object>> videoAddList = objectRedisTemplate.opsForList().range(Constant.VIDEO_ADD_KEY, 0, -1);
        List<HashMap<String, Object>> videoDeleteList = objectRedisTemplate.opsForList().range(Constant.VIDEO_DELETE_KEY, 0, -1);
        List<HashMap<String, Object>> videoUpDateList = objectRedisTemplate.opsForList().range(Constant.VIDEO_UPDATE_KEY, 0, -1);
        List<HashMap<String, Object>> userAddList = objectRedisTemplate.opsForList().range(Constant.USER_ADD_KEY, 0, -1);
        List<HashMap<String, Object>> userUpDateList = objectRedisTemplate.opsForList().range(Constant.USER_UPDATE_KEY, 0, -1);
        //先执行增删同步再进行修改同步，防止修改时该文档已删除或者还未添加
        if (videoAddList.size() > 0) {
            mysqlAddToEs(Constant.OPERATION_ADD, videoAddList, Constant.VIDEO_INDEX_NAME);
        }
        if (videoDeleteList.size() > 0) {
            mysqlAddToEs(Constant.OPERATION_DELETE, videoAddList, Constant.VIDEO_INDEX_NAME);
        }

        if (userAddList.size() > 0) {
            mysqlAddToEs(Constant.OPERATION_ADD, userAddList, Constant.USER_INDEX_NAME);
        }
        SearchRequest videoSearchRequest = new SearchRequest(Constant.VIDEO_INDEX_NAME);
        videoSearchRequest.source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).size(10000));
        SearchResponse videoSearchResponse = client.search(videoSearchRequest, RequestOptions.DEFAULT);
        for (SearchHit searchHit : videoSearchResponse.getHits().getHits()) {
            videoFilter.put(Integer.valueOf(searchHit.getId()));
        }
        SearchRequest userSearchRequest = new SearchRequest(Constant.USER_INDEX_NAME);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder().size(10000);
        userSearchRequest.source(sourceBuilder.query(QueryBuilders.matchAllQuery()));
        SearchResponse userSearchResponse = client.search(userSearchRequest, RequestOptions.DEFAULT);
        //将当前所有文档id存储到布隆过滤器中
        for (SearchHit searchHit : userSearchResponse.getHits().getHits()) {
            int id = Integer.valueOf(searchHit.getId());
            userFilter.put(id);
        }
        //最后再执行更新操作
        if (videoUpDateList.size() > 0) {
            mysqlAddToEs(Constant.OPERATION_UPDATE, videoUpDateList, Constant.VIDEO_INDEX_NAME);
        }
        if (userUpDateList.size() > 0) {
            mysqlAddToEs(Constant.OPERATION_UPDATE, userUpDateList, Constant.USER_INDEX_NAME);
        }
        //删除对应的键值对
        objectRedisTemplate.delete(Constant.VIDEO_ADD_KEY);
        objectRedisTemplate.delete(Constant.VIDEO_DELETE_KEY);
        objectRedisTemplate.delete(Constant.VIDEO_UPDATE_KEY);
        objectRedisTemplate.delete(Constant.USER_UPDATE_KEY);
        objectRedisTemplate.delete(Constant.USER_ADD_KEY);

    }/**
     *根据索引名和原始请求批量添加请求和保存原始请求
     */

    public Boolean mysqlAddToEs(String requestType, List<HashMap<String, Object>> list, String indexName) throws IOException {
        //批量添加请求
        BulkRequest bulkRequest;
        List<DocWriteRequest> docWriteRequestList = new ArrayList<>();
        if (requestType.equals("add")) {
            bulkRequest = new BulkRequest();
            for (Map<String, Object> document : list) {
                int intId;
                if (Constant.VIDEO_INDEX_NAME.equals(indexName)) {
                    intId = (Integer) document.get(Constant.VIDEO_INDEX_ID);
                } else {
                    intId = (Integer) document.get(Constant.INDEX_ID);
                }
                String id = String.valueOf(intId);
                IndexRequest indexRequest = new IndexRequest(indexName);
                indexRequest.id(id).source(document, XContentType.JSON);
                docWriteRequestList.add(indexRequest);
                bulkRequest.add(indexRequest);
            }

        } else if (requestType.equals("update")) {
            bulkRequest = new BulkRequest();
            if (Constant.VIDEO_INDEX_NAME.equals(indexName)) {
                for (Map<String, Object> map : list) {
                    int intId = (Integer) map.get(Constant.VIDEO_INDEX_ID);
                    //利用布隆过滤器判定存在可能误判，判定不存在则一定不存在的特性反向百分百找到一定不会出错的文档id并添加到递归同步的请求集合中
                    if (!videoFilter.mightContain(intId)) {

                    } else {
                        UpdateRequest updateRequest = new UpdateRequest(indexName, String.valueOf(intId)).doc(map);
                        bulkRequest.add(updateRequest);
                        docWriteRequestList.add(updateRequest);
                    }
                }
            } else {
                for (Map<String, Object> map : list) {
                    int intId = (Integer) map.get(Constant.INDEX_ID);
                    if (!userFilter.mightContain(intId)) {

                    } else {
                        UpdateRequest updateRequest = new UpdateRequest(indexName, String.valueOf(intId)).doc(map);
                        bulkRequest.add(updateRequest);
                        docWriteRequestList.add(updateRequest);
                    }
                }
            }
        } else {
            bulkRequest = new BulkRequest();
            for (Map<String, Object> document : list) {
                int intId;
                    intId = (Integer) document.get(Constant.VIDEO_INDEX_ID);
                String id = String.valueOf(intId);
                DeleteRequest deleteRequest = new DeleteRequest(indexName, id);
                bulkRequest.add(deleteRequest);
                docWriteRequestList.add(deleteRequest);
            }
        }
        //递归调用同步方法
        bulkOpreateUntilAllSucess(bulkRequest, docWriteRequestList, 10);
        return true;
    }
    /**
     *递归批量同步操作
     */
    public Boolean bulkOpreateUntilAllSucess(BulkRequest bulkRequest, List<DocWriteRequest> list, int maxRetry) throws IOException {
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        //递归将收集到的失败操作统一递归再调用自己方法最大程度减少失败请求次数，同时设定次数为10防止递归过多堆栈溢出
        if (bulkResponse.hasFailures() && maxRetry > 0) {
            BulkRequest bulkRequest1 = new BulkRequest();
            for (int i = 0; i < bulkResponse.getItems().length; i++) {
                BulkItemResponse itemResponse = bulkResponse.getItems()[i];
                if (itemResponse.isFailed()) {
                    bulkRequest1.add(list.get(i));
                }
            }
            return bulkOpreateUntilAllSucess(bulkRequest, list, maxRetry - 1);
        } else {
            return true;
        }
    }
}
