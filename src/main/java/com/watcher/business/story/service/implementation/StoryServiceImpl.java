package com.watcher.business.story.service.implementation;

import com.watcher.business.board.mapper.BoardMapper;
import com.watcher.business.comm.service.FileService;
import com.watcher.business.story.mapper.StoryMapper;
import com.watcher.business.comm.param.FileParam;
import com.watcher.business.story.param.StoryParam;
import com.watcher.business.story.service.StoryService;
import com.watcher.enums.ResponseCode;
import com.watcher.util.RecommendUtil;
import com.watcher.util.RequestUtil;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class StoryServiceImpl implements StoryService {

    @Autowired
    StoryMapper storyMapper;


    @Autowired
    BoardMapper boardMapper;


    @Autowired
    FileService fileService;


    private String fileUploadPath = "/story";


    @Value("${upload.temp-storage.index01}")
    private String tempStoragePath;


    // 관련게시물 조회 최대 갯수
    private int FeaturedPostListMax = 4;


    @Override
    public void validation(StoryParam storyParam) throws Exception {

    }


    @Transactional
    @Override
    public Map<String, String> insertStory(StoryParam storyParam) throws Exception {
        LinkedHashMap result = new LinkedHashMap();

        // 스토리 내용에 script 제거
        storyParam.setSummary(RequestUtil.cleanXSS(storyParam.getSummary()));
        storyParam.setContents(RequestUtil.cleanXSS(storyParam.getContents()));

        // 스토리 등록
        if( storyParam.getId() == null || storyParam.getId().isEmpty() ){
            storyMapper.insert(storyParam);
        }else{
            storyParam.setUptId(storyParam.getRegId());
            storyMapper.update(storyParam);
        }

        // 태그등록
        if( !(storyParam.getTags() == null || storyParam.getTags().isEmpty()) ){
            Map<String,Object> tagParam = new LinkedHashMap<String,Object>();
            List<String> tagList = Arrays.asList(storyParam.getTags().split(","));

            tagParam.put("contentsType" , "STORY"                   );
            tagParam.put("contentsId"   , storyParam.getId()        );
            tagParam.put("tags"         , tagList                   );
            tagParam.put("regId"        , storyParam.getRegId()     );
            tagParam.put("uptId"        , storyParam.getUptId()     );

            if( storyParam.getId() == null || storyParam.getId().isEmpty() ){
                boardMapper.insertTag(tagParam);
            }else{
                boardMapper.deleteTag(tagParam);

                for(String tag : tagList ){
                    tagParam.put("tag", tag);
                    boardMapper.updateTag(tagParam);
                }
            }
        }

        // 업로드 파일등록
        if( !storyParam.getThumbnailImgPathParam().isEmpty() ){
            FileParam fileParam = new FileParam();
            fileParam.setContentsId(storyParam.getId());
            fileParam.setContentsType("STORY");
            fileParam.setRegId(storyParam.getRegId());
            fileParam.setUptId(storyParam.getRegId());

            int fileId = fileService.uploadAfterSavePath(
                    storyParam.getThumbnailImgPathParam(),
                    fileUploadPath,
                    fileParam
            );

            storyParam.setThumbnailImgId(String.valueOf(fileId));
            storyMapper.update(storyParam);

        }

        result.put("storyId", storyParam.getId());
        result.put("code"   , ResponseCode.SUCCESS_0000.getCode());
        result.put("message", ResponseCode.SUCCESS_0000.getMessage());

        return result;
    }


    @Transactional
    @Override
    public Map<String, String> updateStory(StoryParam storyParam) throws Exception {
        LinkedHashMap result = new LinkedHashMap();
        storyMapper.update(storyParam);

        result.put("code", ResponseCode.SUCCESS_0000.getCode());
        result.put("message", ResponseCode.SUCCESS_0000.getMessage());

        return result;
    }


    @Transactional
    @Override
    public void updateStorysPublic(StoryParam storyParam) throws Exception {
        storyParam.setSecretYn("N");

        JSONArray storyIds = new JSONArray(storyParam.getParamJson());

        storyParam.setIdList(storyIds.toList());
        storyMapper.update(storyParam);
    }


    @Transactional
    @Override
    public void updateStorysPrivate(StoryParam storyParam) throws Exception {
        storyParam.setSecretYn("Y");

        JSONArray storyIds = new JSONArray(storyParam.getParamJson());

        storyParam.setIdList(storyIds.toList());
        storyMapper.update(storyParam);
    }


    @Transactional
    @Override
    public Map<String, String> updateStorys(StoryParam storyParam) throws Exception {
        LinkedHashMap result = new LinkedHashMap();

        JSONArray storyIds = new JSONArray(storyParam.getParamJson());

        storyParam.setIdList(storyIds.toList());
        storyMapper.update(storyParam);

        result.put("code", ResponseCode.SUCCESS_0000.getCode());
        result.put("message", ResponseCode.SUCCESS_0000.getMessage());

        return result;
    }


    @Transactional
    @Override
    public Map<String, String> deleteStory(StoryParam storyParam) throws Exception {
        LinkedHashMap result = new LinkedHashMap();
        storyParam.setDeleteYn("Y");
        storyMapper.update(storyParam);

        result.put("code", ResponseCode.SUCCESS_0000.getCode());
        result.put("message", ResponseCode.SUCCESS_0000.getMessage());

        return result;
    }


    @Transactional
    @Override
    public Map<String, String> deleteStorys(StoryParam storyParam) throws Exception {
        LinkedHashMap result = new LinkedHashMap();

        JSONArray storyIds = new JSONArray(storyParam.getParamJson());

        storyParam.setIdList(storyIds.toList());
        storyParam.setDeleteYn("Y");
        storyMapper.update(storyParam);

        result.put("code", ResponseCode.SUCCESS_0000.getCode());
        result.put("message", ResponseCode.SUCCESS_0000.getMessage());

        return result;
    }


    @Override
    public List<Map<String, Object>> getListManagemenPopular(StoryParam storyParam) throws Exception {
        storyParam.setSearch_secret_yn("ALL");
        storyParam.setSortByRecommendationYn("YY");
        storyParam.setLimitNum("4");
        return storyMapper.selectStory(storyParam);
    }


    @Override
    public Map<String, Object> getListManagement(StoryParam storyParam) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        storyParam.setSearch_secret_yn("ALL");
        storyParam.setTotalCnt( storyMapper.selectStoryCnt(storyParam) );
        result.put("list", storyMapper.selectStory(storyParam));

        result.put("code", ResponseCode.SUCCESS_0000.getCode());
        result.put("message", ResponseCode.SUCCESS_0000.getMessage());

        return result;
    }


    @Override
    public Map<String, Object> getListStoryPublic(StoryParam storyParam) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        storyParam.setSearch_secret_yn("NN");
        storyParam.setTotalCnt( storyMapper.selectStoryCnt(storyParam) );
        result.put("list", storyMapper.selectStory(storyParam));

        result.put("code", ResponseCode.SUCCESS_0000.getCode());
        result.put("message", ResponseCode.SUCCESS_0000.getMessage());

        return result;
    }


    @Override
    public List<Map<String, Object>> getListMyStory(String sessionMemId, StoryParam storyParam) throws Exception {
        if( sessionMemId != null && sessionMemId.equals(storyParam.getSearch_memId()) ){
            storyParam.setSearch_secret_yn("ALL");
        }

        return this.getList(storyParam);
    }


    @Override
    public List<Map<String, Object>> getList(StoryParam storyParam) throws Exception {
        storyParam.setTotalCnt( storyMapper.selectStoryCnt(storyParam) );
        return storyMapper.selectStory(storyParam);
    }


    @Override
    public Map<String, Object> getData(StoryParam storyParam) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("view", storyMapper.view(storyParam));

        result.put("code", ResponseCode.SUCCESS_0000.getCode());
        result.put("message", ResponseCode.SUCCESS_0000.getMessage());


        return result;
    }


    @Override
    public Map<String, Object> getPopularStoryMain(StoryParam storyParam) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();

        result.put("popularStorys", storyMapper.getPopularStoryMain(storyParam));

        result.put("code", ResponseCode.SUCCESS_0000.getCode());
        result.put("message", ResponseCode.SUCCESS_0000.getMessage());

        return result;
    }


    @Override
    public void insertViewsCount(StoryParam storyParam) throws Exception {
        storyMapper.updateViewCountUp(Integer.valueOf(storyParam.getId()));
    }


    @Override
    public void updateLikeCountUp(int id) throws Exception {
        storyMapper.updateLikeCountUp(id);
    }


    @Override
    public void updateLikeCountDown(int id) throws Exception {
        storyMapper.updateLikeCountDown(id);
    }


    @Override
    public List<Map<String, Object>> getFeaturedRelatedPostList(
            String memId,
            String targetContent,
            List<Map<String, Object>> storyList
    ) throws Exception {

        RecommendUtil recommendUtil = new RecommendUtil(tempStoragePath + "/" + memId);
        Map<String, Object> storyRepository = new HashMap<>();
        List<Map<String, Object>> result = new ArrayList<>();

        // 검색대상 문서들 저장
        for(Map<String, Object> obj : storyList){
            String storyKey     = obj.get("ID").toString();
            String storyContent = obj.get("CONTENTS").toString();

            storyRepository.put(storyKey, obj);
            recommendUtil.addDocument(storyKey, storyContent);
        }

        // 유사문서 검색
        String newHtmlDocument = targetContent;
        List<String> recommendations = recommendUtil.searchSimilarDocuments(newHtmlDocument, FeaturedPostListMax);

        // 반환할 유사 스토리 세팅
        for (String docId : recommendations) {
            result.add((Map<String, Object>) storyRepository.get(docId));
        }

        return result;

    }

}
