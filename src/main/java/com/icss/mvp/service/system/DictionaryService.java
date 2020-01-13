package com.icss.mvp.service.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.dao.system.IDictionaryDao;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.entity.system.DictEntryItemEntity;
import com.icss.mvp.entity.system.EntryEntity;
import com.icss.mvp.entity.system.EntryItemEntity;
import com.icss.mvp.util.MybatisUtils;

/**
 * Created by Ray on 2019/3/19.
 *
 * @author Ray
 * @date 2019/3/19 15:54
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service("dictionaryService")
public class DictionaryService {

    private static Logger  logger = Logger.getLogger(DictionaryService.class);

    @Autowired
    private IDictionaryDao dictionaryDao;

    public DictEntryItemEntity getItem(String entryCode, String key) {
        Map<String, Object> parameter = new HashMap<>(0);
        parameter.put("order", "`order`");
        parameter.put("sort", "ASC");

        DictEntryItemEntity entryItem = new DictEntryItemEntity();
        entryItem.setEntryCode(entryCode);
        entryItem.setKey(key);

        getItemConditions(parameter, entryItem);

        List<DictEntryItemEntity> data = null;
        try {
            data = dictionaryDao.getEntryItems(parameter);
        } catch (Exception e) {
            logger.error("dictionaryDao.getEntryItems exception, error: " + e.getMessage() + ", params: "
                         + JSON.toJSONString(parameter));
        }

        if (data == null || data.isEmpty()) {
            data = new ArrayList<>();
        }

        return data.get(0);
    }

    public List<DictEntryItemEntity> getItems(String entryCode, String... keys) {
        Map<String, Object> parameter = new HashMap<>(0);
        parameter.put("order", "`order`");
        parameter.put("sort", "ASC");

        DictEntryItemEntity entryItem = new DictEntryItemEntity();
        entryItem.setEntryCode(entryCode);
        if (keys.length > 0) {
            entryItem.setKey(StringUtils.join(keys, ","));
        }

        getItemConditions(parameter, entryItem);

        List<DictEntryItemEntity> data = null;
        try {
            data = dictionaryDao.getEntryItems(parameter);
        } catch (Exception e) {
            logger.error("dictionaryDao.getEntryItems exception, error: " + e.getMessage() + ", params: "
                         + JSON.toJSONString(parameter));
        }

        if (data == null || data.isEmpty()) {
            data = new ArrayList<>();
        }

        return data;
    }

    public PageResponse<EntryEntity> describeEntries(EntryEntity entry, PageRequest pager) {
        Map<String, Object> parameter = new HashMap<>(0);
        parameter.put("order", "d.`update_time`");
        parameter.put("sort", "DESC");

        getEntryConditions(parameter, entry);

        if (pager == null) {
            pager = new PageRequest();
        }

        PageResponse<EntryEntity> result = new PageResponse<>();

        List<EntryEntity> data = null;
        try {
            Page page = PageHelper.startPage((pager.getPageNumber()) + 1, pager.getPageSize());
            data = dictionaryDao.getEntries(parameter);

            result.setTotalCount((int) page.getTotal());
        } catch (Exception e) {
            logger.error("dictionaryDao.getEntries exception, error: " + e.getMessage() + ", params: "
                         + JSON.toJSONString(parameter));
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        if (data == null || data.isEmpty()) {
            data = new ArrayList<>();
        }
        result.setData(data);

        result.setPageSize(pager.getPageSize());
        result.setPageNumber(pager.getPageNumber() < 0 ? 0 : pager.getPageNumber());

        return result;
    }

    public ListResponse<EntryEntity> describeEntries(EntryEntity entry) {
        Map<String, Object> parameter = new HashMap<>(0);
        parameter.put("order", "d.`update_time`");
        parameter.put("sort", "DESC");

        getEntryConditions(parameter, entry);

        ListResponse<EntryEntity> result = new ListResponse<>();

        List<EntryEntity> data = null;
        try {
            data = dictionaryDao.getEntries(parameter);
        } catch (Exception e) {
            logger.error("dictionaryDao.getEntries exception, error: " + e.getMessage() + ", params: "
                         + JSON.toJSONString(parameter));
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        if (data == null || data.isEmpty()) {
            data = new ArrayList<>();
        }
        result.setData(data);

        return result;
    }

    public PageResponse<DictEntryItemEntity> describeEntryItems(DictEntryItemEntity entryItem, PageRequest pager) {
        Map<String, Object> parameter = new HashMap<>(0);
        parameter.put("order", "`order`");
        parameter.put("sort", "ASC");

        getItemConditions(parameter, entryItem);

        PageResponse<DictEntryItemEntity> result = new PageResponse<>();

        List<DictEntryItemEntity> data = null;
        try {
            Page page = PageHelper.startPage((pager.getPageNumber()) + 1, pager.getPageSize());
            data = dictionaryDao.getEntryItems(parameter);

            result.setTotalCount((int) page.getTotal());
        } catch (Exception e) {
            logger.error("dictionaryDao.getEntryItems exception, error: " + e.getMessage() + ", params: "
                         + JSON.toJSONString(parameter));
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        if (data == null || data.isEmpty()) {
            data = new ArrayList<>();
        }
        result.setData(data);

        result.setPageSize(pager.getPageSize());
        result.setPageNumber(pager.getPageNumber() < 0 ? 0 : pager.getPageNumber());

        return result;
    }

    public ListResponse<DictEntryItemEntity> describeEntryItems(DictEntryItemEntity entryItem) {
        Map<String, Object> parameter = new HashMap<>(0);
        parameter.put("order", "`order`");
        parameter.put("sort", "ASC");

        getItemConditions(parameter, entryItem);

        ListResponse<DictEntryItemEntity> result = new ListResponse<>();

        List<DictEntryItemEntity> data = null;
        try {
            data = dictionaryDao.getEntryItems(parameter);
        } catch (Exception e) {
            logger.error("dictionaryDao.getEntryItems exception, error: " + e.getMessage() + ", params: "
                         + JSON.toJSONString(parameter));
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        if (data == null || data.isEmpty()) {
            data = new ArrayList<>();
        }

        result.setData(data);
        result.setTotalCount(data.size());

        return result;
    }

    public List<DictEntryItemEntity> listAllEntryItems(DictEntryItemEntity entryItem) {

        Map<String, Object> parameter = new HashMap<>(0);
        parameter.put("order", "`order`");
        parameter.put("sort", "ASC");

        getItemConditions(parameter, entryItem);

        ListResponse<DictEntryItemEntity> result = describeEntryItems(entryItem);

        return result == null || CollectionUtils.isEmpty(result.getData()) ? new ArrayList<>(0) : result.getData();
    }

    public ListResponse<String> getItemNames(DictEntryItemEntity entryItem) {
        ListResponse<String> result = new ListResponse<>();

        ListResponse<DictEntryItemEntity> response = describeEntryItems(entryItem);
        if (!response.getSucceed()) {
            result.setErrorMessage(response.getCode(), response.getMessage());
            return result;
        }

        List<String> data = response.getData().stream().map(EntryItemEntity::getKey).collect(Collectors.toList());

        result.setData(data);
        result.setTotalCount(data.size());

        return result;
    }

    private void getItemConditions(Map<String, Object> parameter, DictEntryItemEntity entryItem) {
        if (entryItem != null) {
            MybatisUtils.buildParam(parameter, entryItem.getId(), "id");
            MybatisUtils.buildScopeParam(parameter, entryItem.getKey(), "key");
            MybatisUtils.buildParam(parameter, entryItem.getEntryId(), "entryId");
            MybatisUtils.buildParam(parameter, entryItem.getEntryName(), "entryName");
            MybatisUtils.buildParam(parameter, entryItem.getEntryCode(), "entryCode");
        }
    }

    private void getEntryConditions(Map<String, Object> parameter, EntryEntity entry) {
        if (entry != null) {
            MybatisUtils.buildParam(parameter, entry.getId(), "id");
            MybatisUtils.buildParam(parameter, entry.getName(), "name");
            MybatisUtils.buildParam(parameter, entry.getCode(), "code");
        }
    }

    public BaseResponse increaseEntry(EntryEntity entry, String userId) {
        BaseResponse result = new BaseResponse();

        try {
            dictionaryDao.insertEntry(Collections.singletonList(entry), userId);
        } catch (Exception e) {
            logger.error("dictionaryDao.insertEntry exception, error: " + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

    public BaseResponse modifyEntry(EntryEntity entry) {
        BaseResponse result = new BaseResponse();

        Map<String, Object> params = new HashMap<>(0);
        params.put("id", entry.getId());
        params.put("name", entry.getName());
        params.put("code", entry.getCode());
        params.put("description", entry.getDescription());
        params.put("isDeleted", entry.getIsDeleted());

        params.put("modifyTime", new Date());

        try {
            dictionaryDao.updateEntry(params);
        } catch (Exception e) {
            logger.error("dictionaryDao.updateEntry exception, error: " + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

    public BaseResponse removeEntry(Set<Integer> ids) {
        BaseResponse result = new BaseResponse();

        Map<String, Object> params = new HashMap<>(0);
        params.put("ids", ids);
        params.put("isDeleted", 1);

        params.put("modifyTime", new Date());

        try {
            dictionaryDao.updateEntry(params);
        } catch (Exception e) {
            logger.error("dictionaryDao.updateEntry exception, error: " + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

    public BaseResponse increaseEntryItem(EntryItemEntity entity, String userId) {
        BaseResponse result = new BaseResponse();

        try {
            dictionaryDao.insertEntryItem(Collections.singletonList(entity), userId);
        } catch (Exception e) {
            logger.error("dictionaryDao.insertEntryItem exception, error: " + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

    public BaseResponse modifyEntryItem(EntryItemEntity entity) {
        BaseResponse result = new BaseResponse();

        Map<String, Object> params = new HashMap<>(0);
        params.put("id", entity.getId());
        params.put("key", entity.getKey());
        params.put("value", entity.getValue());
        params.put("order", entity.getOrder());
        params.put("isDeleted", entity.getIsDeleted());

        params.put("modifyTime", new Date());

        try {
            dictionaryDao.updateEntryItem(params);
        } catch (Exception e) {
            logger.error("dictionaryDao.updateEntryItem exception, error: " + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

    public BaseResponse removeEntryItem(Set<Integer> ids) {
        BaseResponse result = new BaseResponse();

        Map<String, Object> params = new HashMap<>(0);
        params.put("ids", ids);
        params.put("isDeleted", 1);

        params.put("modifyTime", new Date());

        try {
            dictionaryDao.updateEntryItem(params);
        } catch (Exception e) {
            logger.error("dictionaryDao.updateEntryItem exception, error: " + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

    public BaseResponse adjustItemOrder(List<EntryItemEntity> entities) {
        BaseResponse result = new BaseResponse();

        try {
            dictionaryDao.adjustItemOrder(entities);
        } catch (Exception e) {
            logger.error("dictionaryDao.adjustItemOrder exception, error: " + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }
}
