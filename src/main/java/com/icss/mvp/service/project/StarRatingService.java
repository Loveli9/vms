package com.icss.mvp.service.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import com.icss.mvp.constant.ERatingStar;
import com.icss.mvp.dao.IProjectInfoDao;
import com.icss.mvp.dao.IStarRatingDao;
import com.icss.mvp.entity.PlainRequest;
import com.icss.mvp.entity.ProjectInfo;
import com.icss.mvp.entity.common.request.PageRequest;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.util.CollectionUtilsLocal;

@Service
@EnableScheduling
@SuppressWarnings("all")
public class StarRatingService {

    private static Logger   logger = Logger.getLogger(StarRatingService.class);

    @Autowired
    private IStarRatingDao  starRatingDao;

    @Autowired
    private IProjectInfoDao projectInfoDao;

    public PageResponse<ProjectInfo> describeStarRatings(ProjectInfo project, String clientType, PageRequest pager) {
        // series 传入值为 1 是按中软组织结构查询，其他都按华为组织机构处理
        boolean isDefault = !"1".equals(clientType);

        List<String> depts = CollectionUtilsLocal.splitToList(isDefault ? project.getPduSpdt() : project.getDu());
        List<String> areas = CollectionUtilsLocal.splitToList(project.getArea());

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("client", clientType);
        parameter.put("depts", depts);
        parameter.put("areas", areas);
        if (!ERatingStar.ALL.title.equals(project.getStarType())
            && !ERatingStar.ZERO.title.equals(project.getStarType())) {
            parameter.put("star", project.getStarType());
        }

        List<ProjectInfo> data = new ArrayList<>();
        // 需要全量查询而不是分页查询结果时，可以传入空值pager
        // 第一步获取符合条件的记录总数，全量查询跳过这一步
        // 跟符合条件的记录总数为0的情况做区分，全量查询设定total值为 -1
        int total = pager == null ? -1 : countTotal(parameter);
        if (pager == null) {
            pager = new PageRequest();
        }

        // 符合查询条件的结果为0，跳过第二步实体列表查询
        if (total != 0) {
            // 总数大于零，设定offset和limit，获取指定页记录
            if (total > 0) {
                parameter.put("offset", pager.getOffset(total));
                parameter.put("limit", pager.getPageSize());
            }

            parameter.put("order", "START_DATE, END_DATE, NAME");
            parameter.put("sort", "ASC");

            data = projectInfoDao.queryStarRating(parameter);
        }

        if (data == null || data.isEmpty()) {
            data = new ArrayList<>();
        }

        PageResponse<ProjectInfo> result = new PageResponse<>();
        result.setData(data);
        result.setPageSize(pager.getPageSize());
        result.setPageNumber(pager.getPageNumber()==null?0:pager.getPageNumber());
        result.setTotalCount(total == -1 ? data.size() : total);

        return result;
    }

    private int countTotal(Map<String, Object> parameter) {
        int total;
        try {
            total = projectInfoDao.countByClient(parameter);
        } catch (Exception e) {
            logger.error("projectInfoDao.countByClient exception, error: "+e.getMessage());
            total = 0;
        }
        return total;
    }

    public PlainRequest<Map> summarizeRating(ProjectInfo project, String clientType) {
        PageResponse<ProjectInfo> response = describeStarRatings(project, clientType, null);
        List<ProjectInfo> list = response.getData();
        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
        }

        // result.put("sumPros", list.size());
        // result.put("sumList", list.stream().map(o -> new HashMap<String, Object>() {
        //
        // {
        // put("no", o.getNo());
        // put("level", o.getStarType());
        // }
        //
        // }));

        Map summarize = new HashMap<>();
        for (ERatingStar rating : ERatingStar.values()) {
            if (rating == ERatingStar.ZERO) {
                summarize.put(rating.toString().toLowerCase(), 0);
                continue;
            }
            if (rating == ERatingStar.ALL) {
                continue;
            }
            List<String> collections = list.stream().filter(o -> rating.title.equalsIgnoreCase(o.getStarType())).map(ProjectInfo::getNo).collect(Collectors.toList());
            summarize.put(rating.toString().toLowerCase(), collections.size());
            // summarize.put(rating.toString(), collections);
        }

        PlainRequest<Map> result = new PlainRequest<>();
        result.setData(summarize);

        return result;
    }
}
