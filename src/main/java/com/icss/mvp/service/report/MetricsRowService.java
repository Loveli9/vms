package com.icss.mvp.service.report;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.report.MetricsRowDao;
import com.icss.mvp.entity.IterationCycle;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.entity.member.MemberEntity;
import com.icss.mvp.entity.report.*;
import com.icss.mvp.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author chenjiabin
 * @createtime 2019-12-11
 */
@Service
@Transactional
public class MetricsRowService extends ServiceImpl<MetricsRowDao, MetricsRow> implements IService<MetricsRow> {
    @Autowired
    private MetricsItemService metricsItemService;
    @Autowired
    private MemberService memberService;

    /**
     * 根据度量表ID查询关联的所有行
     *
     * @param id
     * @return
     */
    public List<MetricsRow> getByMetricesTableId(Integer id) {
        QueryWrapper condition = Wrappers.query().eq("metrics_table_id", id);
        return getBaseMapper().selectList(condition);
    }

    @Transactional(rollbackFor = Exception.class)
    public MetricsRow merge(MetricsRow metricsRow) {
        if (metricsRow.getId() == null) {
            getBaseMapper().insert(metricsRow);
        } else {
            getBaseMapper().updateById(metricsRow);
            List<MetricsItem> metricsItems = metricsItemService.getByMetricesRowId(metricsRow.getId());
            metricsItemService.clearMetricsItem(metricsItems, metricsRow.getMetricsItems());
        }
        if (metricsRow.getMetricsItems() != null && !metricsRow.getMetricsItems().isEmpty()) {
            for (MetricsItem mic : metricsRow.getMetricsItems()) {
                mic.setMetricsRowId(metricsRow.getId());
                metricsItemService.save(mic);
            }
        }
        return metricsRow;
    }

    public boolean deleteByMetricesTableId(Serializable metricesTableId) {

        QueryWrapper condition = Wrappers.query().eq("metrics_Table_Id", metricesTableId);
        List<MetricsRow> rows = list(condition);
        rows.forEach(row -> {
            removeById(metricesTableId);
        });
        return remove(condition);
    }

    @Override
    public boolean removeById(Serializable id) {
        super.removeById(id);
        metricsItemService.deleteByMetricesRowId(id);
        return true;
    }

    public MetricsRow getFullById(Integer id) {
        MetricsRow row = this.getById(id);
        row.setMetricsItems(metricsItemService.getByMetricesRowId(id));
        return row;
    }

    public ListResponse<MetricsRow> getFullPageById(Page page, Integer id) {
        List<MetricsRow> rows = this.getByMetricesTableId(id);
        if (rows != null && !rows.isEmpty()) {
            rows.forEach(row -> {
                List<MetricsItem> items = metricsItemService.getByMetricesRowId(row.getId());
                row.setMetricsItems(items);
            });
        }
        ListResponse response = PageResponse.ok(pageQuering(page, rows)).totalCount(rows.size()).pageNumber(page.getCurrent()).pageSize(page.getSize());
        return response;
    }

    /**
     * 分页
     */
    private List<MetricsRow> pageQuering(Page page, List<MetricsRow> metricsRows) {
        List<MetricsRow> outList = new ArrayList<>();
        long current = page.getCurrent();
        long size = page.getSize();
        long start = (current - 1) * size;
        long end = current * size - 1;
        for (long i = start; i <= end; i++) {
            outList.add(metricsRows.get(Integer.parseInt(i + "")));
        }
        return outList;
    }

    public int reportProjectMetricsRow(MetricsTable metricsTable, Integer metricsTableId, List<MetricsItemConfig> mics) {
        ListResponse<MemberEntity> members = memberService.getProjectMembers(metricsTable.getProjectId(), new HashSet<>(0));
        if (members.getData() != null && !members.getData().isEmpty()) {
            for (MemberEntity member : members.getData()) {
                MetricsRow row = new MetricsRow();
                row.setMetricsTableId(metricsTableId);
                row.setMetricsItems(new ArrayList<>());
                mics.forEach(mic -> {
                    MetricsItem mi = new MetricsItem();
                    mi.setMetricsItemConfigId(mic.getId());
                    row.getMetricsItems().add(mi);
                });
                row.setCode(member.getAccount());
                row.setName(member.getName());
                //TODO 发起采集任务
                merge(row);
            }
        }
        return 1;
    }

    public int reportIterationMetricsRows(MetricsTable metricsTable, Integer metricsTableId, List<MetricsRow> existedMetricsRows, List<MetricsItemConfig> metricsItemConfigs, List<IterationCycle> iterationCycles) {
        List<MetricsRow> rows = new ArrayList<>();
        List<String> existedIterationIds = new ArrayList<String>() {{
            if (existedMetricsRows != null) {
                existedMetricsRows.forEach(mr -> {
                    add(mr.getPeriodId());
                });
            }
        }};
        List<IterationCycle> missingIterations = new ArrayList<IterationCycle>() {{
            if (iterationCycles != null) {
                iterationCycles.forEach(iteration -> {
                    if (!existedIterationIds.contains(iteration.getId())) {
                        add(iteration);
                    }
                });
            }
        }};
        for (IterationCycle iteration : missingIterations) {
            ListResponse<MemberEntity> members = memberService.getProjectMembers(metricsTable.getProjectId(), new HashSet<>(0));
            if ("人员".equalsIgnoreCase(metricsTable.getType())) {
                if (members.getData() != null && !members.getData().isEmpty()) {
                    for (MemberEntity member : members.getData()) {
                        MetricsRow row = newMetricsRow(metricsTableId, metricsItemConfigs, iteration);
                        row.setCode(member.getAccount());
                        row.setName(member.getName());
                        rows.add(row);
                    }
                }
            } else if ("需求".equalsIgnoreCase(metricsTable.getType())) {
                //TODO 加载项目迭代内的需求列表，按需求列表生成row
            } else {
                MetricsRow row = newMetricsRow(metricsTableId, metricsItemConfigs, iteration);
                rows.add(row);
            }
        }
        for (MetricsRow row : rows) {
            merge(row);
        }
        return rows.size();
    }

    private MetricsRow newMetricsRow(Integer metricsTableId, List<MetricsItemConfig> metricsItemConfigs, IterationCycle iteration) {
        MetricsRow row = new MetricsRow();
        row.setMetricsTableId(metricsTableId);
        row.setPeriod(MetricsTableConfig.PERIOD_ITERATION);
        row.setPeriodId(iteration.getId());
        row.setPeriodName(iteration.getIteName());
        row.setMetricsItems(new ArrayList<>());
        metricsItemConfigs.forEach(mic -> {
            MetricsItem mi = new MetricsItem();
            mi.setMetricsItemConfigId(mic.getId());
            row.getMetricsItems().add(mi);
        });
        return row;
    }
}
