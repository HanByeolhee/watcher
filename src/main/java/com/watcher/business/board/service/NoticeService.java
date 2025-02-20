package com.watcher.business.board.service;

import com.watcher.business.board.param.NoticeParam;

import java.util.List;
import java.util.Map;

public interface NoticeService {
    public List<Map<String,Object>> getListNotice(String sessionMemId, NoticeParam noticeParam) throws Exception;

    public List<Map<String,Object>> getListNotice(NoticeParam noticeParam) throws Exception;

    public Map<String, Object> getData(NoticeParam noticeParam) throws Exception;

    public void insertViewsCount(NoticeParam noticeParam) throws Exception;

    public void updateNoticesPublic(NoticeParam noticeParam) throws Exception;

    public void updateNoticesPrivate(NoticeParam noticeParam) throws Exception;

    public void updates(NoticeParam noticeParam) throws Exception;

    public void delete(NoticeParam noticeParam) throws Exception;

    public void deletes(NoticeParam noticeParam) throws Exception;

    public String insert(NoticeParam noticeParam) throws Exception;

    public void updateLikeCountUp(int id) throws Exception;

    public void updateLikeCountDown(int id) throws Exception;

}
