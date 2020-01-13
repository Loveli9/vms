package com.icss.mvp.controller.system;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.icss.mvp.constant.CommonResultCode;
import com.icss.mvp.controller.BaseController;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.system.DictEntryItemEntity;
import com.icss.mvp.entity.system.EntryEntity;
import com.icss.mvp.entity.system.EntryItemEntity;
import com.icss.mvp.service.system.DictionaryService;
import com.icss.mvp.util.MathUtils;

/**
 * Created by Ray on 2019/3/19.
 *
 * @author Ray
 * @date 2019/3/19 11:17
 */
@Controller
@RequestMapping("/dict")
public class DataDictionaryController extends BaseController {

    private static Logger     logger = Logger.getLogger(DataDictionaryController.class);

    @Autowired
    private DictionaryService dictionaryService;

    @RequestMapping(value = "/entries", method = RequestMethod.GET, consumes = "application/x-www-form-urlencoded")
    @ResponseBody
    public PageResponse<EntryEntity> describeEntries(PageRequest pager, EntryEntity entity) {

        PageResponse<EntryEntity> result = new PageResponse<>();

        try {
            if (entity == null) {
                entity = new EntryEntity();
            }

            // 处理中文乱码
            entity.setName(URLDecoder.decode(entity.getName(), "UTF-8"));
            
            result = dictionaryService.describeEntries(entity, pager);
        } catch (Exception e) {
            logger.error("dictionaryService.describeEntries, error:" + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/entries/{id}", method = RequestMethod.GET, consumes = "application/json")
    @ResponseBody
    public PlainResponse<EntryEntity> describeEntry(@PathVariable String id) {

        PlainResponse<EntryEntity> result = new PlainResponse<>();

        try {
            EntryEntity entity = new EntryEntity();
            entity.setId(id);

            ListResponse<EntryEntity> response = dictionaryService.describeEntries(entity);
            if (response.getSucceed() && response.getTotalCount() > 0) {
                result.setData(response.getData().get(0));
            } else {
                result.setErrorMessage(response.getCode(), response.getMessage());
            }
        } catch (Exception e) {
            logger.error("dictionaryService.describeEntries, error:" + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/items", method = RequestMethod.GET, consumes = "application/x-www-form-urlencoded")
    @ResponseBody
    public ListResponse<DictEntryItemEntity> describeEntryItems(DictEntryItemEntity entity) {

        ListResponse<DictEntryItemEntity> result = new ListResponse<>();

        try {
            if (entity == null) {
                entity = new DictEntryItemEntity();
            }

            entity.setEntryName(urlDecodeISO(entity.getEntryName()));
            // 处理中文乱码
            entity.setKey(entity.getKey() != null ? URLDecoder.decode(entity.getKey(), "UTF-8") : null);

            if (StringUtils.isNotBlank(entity.getEntryCode()) || StringUtils.isNotBlank(entity.getEntryName())
                || StringUtils.isNotBlank(entity.getEntryId()) || StringUtils.isNotBlank(entity.getKey())) {
                result = dictionaryService.describeEntryItems(entity);
            }
        } catch (Exception e) {
            logger.error("dictionaryService.describeEntryItems, error:" + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/entries/{entryId}/items", method = RequestMethod.GET, consumes = "application/x-www-form-urlencoded")
    @ResponseBody
    public ListResponse<DictEntryItemEntity> describeEntryItems(@PathVariable String entryId, DictEntryItemEntity entity) {

        ListResponse<DictEntryItemEntity> result = new ListResponse<>();

        try {
            if (entity == null) {
                entity = new DictEntryItemEntity();
            }

            entity.setEntryId(entryId);
            entity.setEntryName(urlDecodeISO(entity.getEntryName()));
            // 处理中文乱码
            entity.setKey(URLDecoder.decode(entity.getKey(), "UTF-8"));

            result = dictionaryService.describeEntryItems(entity);
        } catch (Exception e) {
            logger.error("dictionaryService.describeEntryItems, error:" + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/items/{id}", method = RequestMethod.GET, consumes = "application/json")
    @ResponseBody
    public PlainResponse<DictEntryItemEntity> describeEntryItem(@PathVariable String id) {

        PlainResponse<DictEntryItemEntity> result = new PlainResponse<>();

        try {
            DictEntryItemEntity entity = new DictEntryItemEntity();
            entity.setId(id);

            ListResponse<DictEntryItemEntity> response = dictionaryService.describeEntryItems(entity);
            if (response.getSucceed() && response.getTotalCount() > 0) {
                result.setData(response.getData().get(0));
            } else {
                result.setErrorMessage(response.getCode(), response.getMessage());
            }
        } catch (Exception e) {
            logger.error("dictionaryService.describeEntryItems, error:" + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/entries/{id}/scope", method = RequestMethod.GET, consumes = "application/json")
    @ResponseBody
    public ListResponse<String> getEntry(@PathVariable String id) {
        ListResponse<String> result = new ListResponse<>();

        try {
            DictEntryItemEntity entity = new DictEntryItemEntity();
            entity.setEntryId(id);

            result = dictionaryService.getItemNames(entity);
        } catch (Exception e) {
            logger.error("dictionaryService.getItemNames, error:" + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/entries/create", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public BaseResponse increaseEntry(@RequestBody EntryEntity entity) {
        BaseResponse result = new BaseResponse();

        try {
            result = dictionaryService.increaseEntry(entity, getCurrentUser().getId());
        } catch (Exception e) {
            logger.error("dictionaryService.increaseEntry, error:" + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/entries/{id}/update", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public BaseResponse modifyEntry(@PathVariable String id, @RequestBody EntryEntity entity) {
        BaseResponse result = new BaseResponse();
        if (StringUtils.isBlank(id)) {
            result.setError(CommonResultCode.INVALID_PARAMETER, "id");
            return result;
        }

        try {
            if (entity == null) {
                entity = new EntryEntity();
            }
            entity.setId(id);
            entity.setIsDeleted(0);

            result = dictionaryService.modifyEntry(entity);
        } catch (Exception e) {
            logger.error("dictionaryService.modifyEntry, error:" + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

    /**
     * 删除类目，传入id数组可进行多条数据的一次性更新
     * 
     * <pre>
     * $.ajax({
     *      url: getRootPath() + '/dict/entries/delete',
     *      type: 'post',
     *      dataType: "json",
     *      contentType: 'application/json',
     *      data: JSON.stringify(ids),
     *      //traditional: true,
     *      success: function (data) {
     *           if (data.code == '200') {
     *           //'删除成功！'
     *           } else {
     *           //'删除失败!'
     *           }
     *      }
     * });
     * </pre>
     * 
     * @param ids
     * @return
     */
    @RequestMapping(value = "/entries/delete", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse removeEntry(@RequestBody Set<Integer> ids) {
        BaseResponse result = new BaseResponse();
        if (ids == null || ids.isEmpty()) {
            return result;
        }

        try {
            result = dictionaryService.removeEntry(ids);
        } catch (Exception e) {
            logger.error("dictionaryService.removeEntry, error:" + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/items/create", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public BaseResponse increaseEntryItem(@RequestBody EntryItemEntity entity) {
        BaseResponse result = new BaseResponse();

        try {
            // TODO: do checking for 'key' cannot be null
            entity.setOrder(-1);
            result = dictionaryService.increaseEntryItem(entity, getCurrentUser().getId());
        } catch (Exception e) {
            logger.error("dictionaryService.increaseEntryItem, error:" + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/items/{id}/update", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public BaseResponse modifyEntryItem(@PathVariable String id, @RequestBody EntryItemEntity entity) {
        BaseResponse result = new BaseResponse();
        if (StringUtils.isBlank(id)) {
            result.setError(CommonResultCode.INVALID_PARAMETER, "id");
            return result;
        }

        try {
            if (entity == null) {
                entity = new EntryItemEntity();
            }
            entity.setId(id);
            entity.setIsDeleted(0);

            result = dictionaryService.modifyEntryItem(entity);
        } catch (Exception e) {
            logger.error("dictionaryService.modifyEntryItem, error:" + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

    @RequestMapping(value = "/items/{entryId}/adjust", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public BaseResponse adjustEntryItemOrder(@PathVariable String entryId, @RequestBody Map<String, Integer> sequence) {
        BaseResponse result = new BaseResponse();
        if (StringUtils.isBlank(entryId)) {
            result.setError(CommonResultCode.INVALID_PARAMETER, "entryId");
            return result;
        }

        if (sequence == null || sequence.isEmpty()) {
            return result;
        }

        try {
            List<EntryItemEntity> list = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : sequence.entrySet()) {
                int id = MathUtils.parseIntSmooth(entry.getKey(), 0);
                Integer order;
                if (id <= 0 || (order = entry.getValue()) == null || order == 0) {
                    continue;
                }

                EntryItemEntity entity = new EntryItemEntity();
                entity.setEntryId(entryId);
                entity.setId(id);
                entity.setOrder(order);

                list.add(entity);
            }

            result = dictionaryService.adjustItemOrder(list);
        } catch (Exception e) {
            logger.error("dictionaryService.adjustItemOrder, error:" + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }

    /**
     * 删除条目，传入id数组可进行多条数据的一次性更新
     * 
     * <pre>
     * $.ajax({
     *      url: getRootPath() + '/dict/items/delete',
     *      type: 'post',
     *      dataType: "json",
     *      contentType: 'application/json',
     *      data: JSON.stringify(ids),
     *      //traditional: true,
     *      success: function (data) {
     *           if (data.code == '200') {
     *           //'删除成功！'
     *           } else {
     *           //'删除失败!'
     *           }
     *      }
     * });
     * </pre>
     * 
     * @param ids
     * @return
     */
    @RequestMapping(value = "/items/delete", method = RequestMethod.POST, consumes = "application/json")
    @ResponseBody
    public BaseResponse removeEntryItem(@RequestBody Set<Integer> ids) {
        BaseResponse result = new BaseResponse();
        if (ids == null || ids.isEmpty()) {
            return result;
        }

        try {
            result = dictionaryService.removeEntryItem(ids);
        } catch (Exception e) {
            logger.error("dictionaryService.removeEntryItem, error:" + e.getMessage());
            result.setError(CommonResultCode.INTERNAL, e.getMessage());
        }

        return result;
    }
}
