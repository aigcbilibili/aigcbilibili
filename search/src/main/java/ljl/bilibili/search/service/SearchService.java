package ljl.bilibili.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import ljl.bilibili.client.pojo.RecommendVideo;
import ljl.bilibili.search.vo.request.EsIndexRequest;
import ljl.bilibili.search.vo.request.EsKeywordRequest;
import ljl.bilibili.search.vo.response.TotalCountSearchResponse;
import ljl.bilibili.search.vo.response.UserKeyWordSearchResponse;
import ljl.bilibili.search.vo.response.VideoKeywordSearchResponse;
import ljl.bilibili.util.Result;

import java.io.IOException;
import java.util.List;

public interface SearchService{
    Result<TotalCountSearchResponse> totalKeywordSearch(String keyword);
    Result<Boolean> addKeywordSearchRecord(EsKeywordRequest esKeywordRequest) throws IOException;
    Result<List<VideoKeywordSearchResponse>> videoPageKeywordSearch(String keyword, int pageNumber, Integer type) throws JsonProcessingException;
    Result<List<UserKeyWordSearchResponse>> userPageKeywordSearch(String keyword, int pageNumber, Integer type, Integer userId) throws JsonProcessingException;
    Result<List<String>> likelyKeywordSearch(String searchWord) throws IOException;
    List<RecommendVideo> likelyVideoRecommend(String videoId) throws IOException;
    Boolean createIndex(EsIndexRequest esIndexRequest);
    Boolean deleteIndex(String indexName) throws IOException;
}
