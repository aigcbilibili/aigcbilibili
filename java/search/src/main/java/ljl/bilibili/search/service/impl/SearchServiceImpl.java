package ljl.bilibili.search.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import ljl.bilibili.entity.user_center.user_relationships.Follow;
import ljl.bilibili.client.pojo.RecommendVideo;
import ljl.bilibili.mapper.user_center.user_relationships.FollowMapper;
import ljl.bilibili.search.service.SearchService;
import ljl.bilibili.search.vo.request.EsKeywordRequest;
import ljl.bilibili.search.vo.response.UserKeyWordSearchResponse;
import ljl.bilibili.util.Result;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder.Item;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ljl.bilibili.search.vo.response.TotalCountSearchResponse;
import ljl.bilibili.search.vo.response.VideoKeywordSearchResponse;
import ljl.bilibili.search.vo.request.EsIndexRequest;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static ljl.bilibili.search.constant.Constant.*;
/**
 *搜索相关
 */
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {
    @Resource
    public RestHighLevelClient client;
    final int size = 20;
    @Resource
    FollowMapper followMapper;
    @Resource
    ObjectMapper objectMapper;
    /**
     *获取用户、视频总匹配文档数和页数
     */
@Override
    public Result<TotalCountSearchResponse> totalKeywordSearch(String keyword) {
    SearchRequest videoSearchRequest = new SearchRequest(VIDEO_INDEX_NAME);
        SearchSourceBuilder videoSearchSourceBuilder = new SearchSourceBuilder();
        videoSearchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, MULTI_QUERY_VIDEO_NAME, MULTI_QUERY_AUTHOR_NAME, MULTI_QUERY_INTRO)
                .type(MultiMatchQueryBuilder.Type.MOST_FIELDS));
        videoSearchSourceBuilder.minScore(1.0f);
        videoSearchSourceBuilder.sort(ORDER_BY_SCORE, SortOrder.DESC);
        videoSearchSourceBuilder.fetchSource(true);
        videoSearchRequest.source(videoSearchSourceBuilder);
        SearchRequest userSearchRequest = new SearchRequest(USER_INDEX_NAME);
        SearchSourceBuilder userSearchSourceBuilder = new SearchSourceBuilder();
        userSearchSourceBuilder.minScore(1.0f);
        userSearchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, MULTI_QUERY_NICKNAME, MULTI_QUERY_INTRO)
                .type(MultiMatchQueryBuilder.Type.MOST_FIELDS));
        userSearchSourceBuilder.sort(ORDER_BY_SCORE, SortOrder.DESC);
        userSearchSourceBuilder.fetchSource(true);
        userSearchRequest.source(userSearchSourceBuilder);
        SearchResponse videoResponse;
        SearchResponse userResponse;
        try {
            videoResponse = client.search(videoSearchRequest, RequestOptions.DEFAULT);
            userResponse = client.search(userSearchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long totalVideoCount = videoResponse.getHits().getTotalHits().value;
        long totalVideoPages = totalVideoCount / size + (totalVideoCount % size == 0 ? 0 : 1);
        long totalUserCount = userResponse.getHits().getTotalHits().value;
        long totalUserPages = totalUserCount / size + (totalUserCount % size == 0 ? 0 : 1);

        return Result.data(new TotalCountSearchResponse().setTotalVideoPage(totalVideoPages).setTotalVideoNum(totalVideoCount)
                .setTotalUserNum(totalUserCount).setTotalUserPage(totalUserPages));
    }
    /**
     *添加搜索记录
     */
    @Override
    public Result<Boolean> addKeywordSearchRecord(EsKeywordRequest esKeywordRequest) throws IOException {
        IndexRequest indexRequest = new IndexRequest(HISTORY_SEARCH_INDEX_NAME);
        indexRequest.source(objectMapper.convertValue(esKeywordRequest, Map.class), XContentType.JSON);
        client.index(indexRequest, RequestOptions.DEFAULT);
        return Result.success(true);
    }
    /**
     *获取某一页的匹配视频数据
     */
    @Override
    public Result<List<VideoKeywordSearchResponse>> videoPageKeywordSearch(String keyword, int pageNumber, Integer type) throws JsonProcessingException {
        List<VideoKeywordSearchResponse> videoKeywordSearchResponses = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(VIDEO_INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, MULTI_QUERY_VIDEO_NAME, MULTI_QUERY_AUTHOR_NAME, MULTI_QUERY_INTRO)
                .type(MultiMatchQueryBuilder.Type.MOST_FIELDS));
        searchSourceBuilder.minScore(1.0f);
        if (type == 0) {
            searchSourceBuilder.sort(ORDER_BY_SCORE, SortOrder.DESC);
        } else if (type == 1) {
            searchSourceBuilder.sort(ORDER_BY_PLAY_COUNT, SortOrder.DESC);
        } else if (type == 2) {
            searchSourceBuilder.sort(ORDER_BY_CREATE_TIME, SortOrder.DESC);
        } else if (type == 3) {
            searchSourceBuilder.sort(ORDER_BY_DANMAKU_COUNT, SortOrder.DESC);
        } else {
            searchSourceBuilder.sort(ORDER_BY_COLLECT_COUNT, SortOrder.DESC);
        }

        searchSourceBuilder.fetchSource(true);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response;
        searchSourceBuilder.size(size);
        searchSourceBuilder.from((pageNumber - 1) * size);
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (SearchHit hit : response.getHits().getHits()) {
            videoKeywordSearchResponses.add(objectMapper.readValue(hit.getSourceAsString(), VideoKeywordSearchResponse.class));
        }
        return Result.data(videoKeywordSearchResponses);
    }
    /**
     *获取某一页的匹配用户数据
     */
    @Override
    public Result<List<UserKeyWordSearchResponse>> userPageKeywordSearch(String keyword, int pageNumber, Integer type, Integer userId) throws JsonProcessingException {
        List<UserKeyWordSearchResponse> userKeyWordSearchResponses = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest(USER_INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(keyword, MULTI_QUERY_NICKNAME, MULTI_QUERY_INTRO)
                .type(MultiMatchQueryBuilder.Type.MOST_FIELDS));
        if (type == 0) {
            searchSourceBuilder.sort(ORDER_BY_SCORE, SortOrder.DESC);
        } else if (type == 1) {
            searchSourceBuilder.sort(ORDER_BY_FANS_COUNT, SortOrder.DESC);
        } else {
            searchSourceBuilder.sort(ORDER_BY_FANS_COUNT, SortOrder.ASC);
        }
        searchSourceBuilder.fetchSource(true);
        searchRequest.source(searchSourceBuilder);
        List<Integer> ids = new ArrayList<>();
        SearchResponse response;
        searchSourceBuilder.size(size);
        searchSourceBuilder.from((pageNumber - 1) * size);
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (SearchHit hit : response.getHits().getHits()) {
            ids.add( Integer.valueOf((String) hit.getSourceAsMap().get(INDEX_ID)));
            userKeyWordSearchResponses.add(objectMapper.readValue(hit.getSourceAsString(), UserKeyWordSearchResponse.class));
        }
        Set<Integer> followSet = new HashSet(10);
        LambdaQueryWrapper<Follow> followLambdaQueryWrapper = new LambdaQueryWrapper<>();
        followLambdaQueryWrapper.eq(Follow::getFansId, userId);
        List<Follow> follows = followMapper.selectList(followLambdaQueryWrapper);
        for (Follow follow : follows) {
            followSet.add(follow.getIdolId());
        }
        userKeyWordSearchResponses.forEach(userKeyWordSearchResponse -> {
            if (followSet.contains(userKeyWordSearchResponse.getId())) {
                userKeyWordSearchResponse.setIsFollow(true);
            } else {
                userKeyWordSearchResponse.setIsFollow(false);
            }
        });
        return Result.data(userKeyWordSearchResponses);
    }
    /**
     *搜索但未确认时类似关键字查询
     */
    @Override
    public Result<List<String>> likelyKeywordSearch(String searchWord) throws IOException {
        SearchRequest searchRequest = new SearchRequest(HISTORY_SEARCH_INDEX_NAME);
        List<String> list = new ArrayList<>();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.multiMatchQuery(searchWord, MULTI_QUERY_SEARCH_WORD));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            EsKeywordRequest keywordRequest = objectMapper.readValue(searchHit.getSourceAsString(), EsKeywordRequest.class);
            list.add(keywordRequest.getSearchWord());
        }
        return Result.data(list);
    }
    /**
     *推荐视频查询
     */
    @Override
    public List<RecommendVideo> likelyVideoRecommend(String videoId) throws IOException {
        //使用了特定查询query--morelikethisquery来强化对关键字匹配度的查询
        SearchRequest searchRequest = new SearchRequest(VIDEO_INDEX_NAME);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        Item[] items = {
                new Item(VIDEO_INDEX_NAME, videoId),
        };
        sourceBuilder.sort(ORDER_BY_SCORE, SortOrder.DESC);
        sourceBuilder.query(QueryBuilders.moreLikeThisQuery(
                        null,
                        null,
                        items
                ).minTermFreq(1)
                .maxQueryTerms(12));
        List<RecommendVideo> list = new ArrayList<>();
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
            RecommendVideo recommendVideo = objectMapper.readValue(searchHit.getSourceAsString(), RecommendVideo.class);
            if (!recommendVideo.getVideoId().equals(videoId)) {
                list.add(recommendVideo);
            }
        }
        return list;
    }
    /**
     *创建索引
     */
    @Override
    public Boolean createIndex(EsIndexRequest esIndexRequest) {
        try {
            String index = esIndexRequest.getIndexName();
            Map<String, String> map = esIndexRequest.getProperties();
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.startObject(INDEX_START_OBJECT_MAPPINGS);
                {
                    builder.startObject(INDEX_START_OBJECT_PROPERTIES);
                    {
                        // 添加其他字段
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            builder.startObject(entry.getKey());
                            {
                                builder.field(INDEX_FIELD_TYPE, entry.getValue());
                            }
                            builder.endObject();
                        }
                    }
                    builder.endObject();
                }
                builder.endObject();
            }
            builder.endObject();
            CreateIndexRequest request = new CreateIndexRequest(index);
            request.source(builder);
            client.indices().create(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    /**
     *删除索引
     */
    @Override
    public Boolean deleteIndex(String indexName) throws IOException {
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
        boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        if (exists) {
            DeleteIndexRequest request = new DeleteIndexRequest(indexName);
            client.indices().delete(request, RequestOptions.DEFAULT);
            System.out.println("索引删除了");
        } else {
            System.out.println("索引不存在");
        }
        return true;
    }
}

