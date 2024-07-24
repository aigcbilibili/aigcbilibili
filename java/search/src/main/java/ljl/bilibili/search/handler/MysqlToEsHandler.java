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
     *每隔15分钟的定时任务，若查询状态为false则先进行全量同步再取出redis中增量消息进行数据同步
     */
    @XxlJob("mysqlToEs")
    public void mysqlToEsHandler() throws Exception {
        if (hasSynchronousVideo == false) {
            mysqlToEsService.videoMysqlToEs();
            hasSynchronousVideo = true;
        }
        if (hasSynchronousUser == false) {
            mysqlToEsService.userMysqlToEs();
            hasSynchronousUser = true;
        }
        List<HashMap<String, Object>> videoAddList = objectRedisTemplate.opsForList().range(Constant.VIDEO_ADD_KEY, 0, -1);
        List<HashMap<String, Object>> videoDeleteList = objectRedisTemplate.opsForList().range(Constant.VIDEO_DELETE_KEY, 0, -1);
        List<HashMap<String, Object>> videoUpDateList = objectRedisTemplate.opsForList().range(Constant.VIDEO_UPDATE_KEY, 0, -1);
        List<HashMap<String, Object>> userAddList = objectRedisTemplate.opsForList().range(Constant.USER_ADD_KEY, 0, -1);
        List<HashMap<String, Object>> userUpDateList = objectRedisTemplate.opsForList().range(Constant.USER_UPDATE_KEY, 0, -1);
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
        for (SearchHit searchHit : userSearchResponse.getHits().getHits()) {
            int id = Integer.valueOf(searchHit.getId());
            userFilter.put(id);
        }
        if (videoUpDateList.size() > 0) {
            mysqlAddToEs(Constant.OPERATION_UPDATE, videoUpDateList, Constant.VIDEO_INDEX_NAME);
        }
        if (userUpDateList.size() > 0) {
            mysqlAddToEs(Constant.OPERATION_UPDATE, userUpDateList, Constant.USER_INDEX_NAME);
        }
        mysqlToEsService.updateUserData();
        mysqlToEsService.updateVideoData();
        objectRedisTemplate.delete(Constant.VIDEO_ADD_KEY);
        objectRedisTemplate.delete(Constant.VIDEO_DELETE_KEY);
        objectRedisTemplate.delete(Constant.VIDEO_UPDATE_KEY);
        objectRedisTemplate.delete(Constant.USER_UPDATE_KEY);
        objectRedisTemplate.delete(Constant.USER_ADD_KEY);

    }/**
     *根据索引名和原始请求批量添加请求和保存原始请求
     */

    public Boolean mysqlAddToEs(String requestType, List<HashMap<String, Object>> list, String indexName) throws IOException {
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
        bulkOpreateUntilAllSucess(bulkRequest, docWriteRequestList, 10);
        return true;
    }
    /**
     *递归批量操作最大程度减少失败操作，并限定最多递归10次防止递归过多栈溢出
     */
    public Boolean bulkOpreateUntilAllSucess(BulkRequest bulkRequest, List<DocWriteRequest> list, int maxRetry) throws IOException {
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
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
