package com.watcher.business.board.service;

import java.util.LinkedHashMap;
import java.util.Map;

public interface BoardService {
    public int getCommentListCnt(LinkedHashMap param) throws Exception;

    public Map<String, Object> getCommentList(LinkedHashMap param) throws Exception;

    public Map<String, Object> getCommentInfo(LinkedHashMap param) throws Exception;

    public Map<String, String> insertComment(LinkedHashMap param) throws Exception;

    public Map<String, String> updateComment(LinkedHashMap param) throws Exception;

    public Map<String, String> deleteComment(LinkedHashMap param) throws Exception;

    public Map<String, String> getTagDatas(String contentsType, String contentsId) throws Exception;

    public Map<String, String> getLikeYn(String contentsType, String contentsId, String loginId) throws Exception;

    public void insertLike(Map<String, Object> param) throws Exception;

    public void updateLike(Map<String, Object> param) throws Exception;
}
