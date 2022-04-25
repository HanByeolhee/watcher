package com.watcher.service;

import com.watcher.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Service
public class BoardService {

    @Autowired
    BoardMapper boardMapper;


    public void views_count(String contentsType, String contentsId, String loginId) throws Exception {

        LinkedHashMap param = new LinkedHashMap();

        param.put("contentsType", contentsType  );
        param.put("contentsId"  , contentsId    );

        boardMapper.views_count(param);

    }


    public List<Map<String, String>> comment_select(String contentsType, String contentsId) throws Exception {

        LinkedHashMap param = new LinkedHashMap();

        param.put("contentsType", contentsType  );
        param.put("contentsId"  , contentsId    );

        return boardMapper.comment_select(param);

    }

    public Map<String, String> view_tags_select(String contentsType, String contentsId) throws Exception {

        LinkedHashMap param = new LinkedHashMap();
        Map<String, String> result = null;

        param.put("contentsType", contentsType  );
        param.put("contentsId"  , contentsId    );

        result = boardMapper.view_tags_select(param);

        if( result == null ){
            result = new LinkedHashMap<>();
            result.put("tags","");

        }

        return result;

    }


    public Map<String, String> view_like_yn_select(String contentsType, String contentsId, String loginId) throws Exception {

        LinkedHashMap param = new LinkedHashMap();
        Map<String, String> result = null;

        param.put("contentsType", contentsType  );
        param.put("contentsId"  , contentsId    );
        param.put("memberId"    , loginId       );

        result = boardMapper.view_like_yn_select(param);

        if( result == null ){
            result = new LinkedHashMap<>();
        }

        return result;

    }

}
