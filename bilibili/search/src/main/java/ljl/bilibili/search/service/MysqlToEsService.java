package ljl.bilibili.search.service;

import java.io.IOException;

public interface MysqlToEsService {
    Boolean userMysqlToEs() throws IOException;
    Boolean videoMysqlToEs() throws IOException;
    Boolean updateVideoData() throws IOException;
    Boolean updateUserData() throws IOException;
}
