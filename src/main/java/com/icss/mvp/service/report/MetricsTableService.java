package com.icss.mvp.service.report;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.report.MetricsTableDao;
import com.icss.mvp.entity.IterationCycle;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.report.*;
import com.icss.mvp.service.IterationCycleService;
import com.icss.mvp.service.ProjectInfoService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author chenjiabin
 * @createtime 2019-12-11
 */
@Service
@Transactional
public class MetricsTableService extends ServiceImpl<MetricsTableDao, MetricsTable> implements IService<MetricsTable> {
    @Autowired
    private MetricsItemService metricsItemService;
    @Autowired
    private MetricsRowService metricsRowService;
    @Autowired
    private MetricsTableConfigService metricsTableConfigService;
    @Autowired
    private IterationCycleService iterationCycleService;
    @Autowired
    private ProjectInfoService projectInfoService;

    /**
     * 查询度量表及其中的行和度量项
     *
     * @param id
     * @param id
     * @return
     */
    public MetricsTable getFullById(Serializable id) {
        MetricsTable metricsTable = super.getById(id);
        loadRows(metricsTable);
        return metricsTable;
    }

    @Override
    public boolean removeById(Serializable id) {
        boolean val = super.removeById(id);
        metricsRowService.deleteByMetricesTableId(id);
        return val;
    }

    public MetricsTable getByProjectIdAndMetricsTableConfigId(String projectId, Integer metricsTableConfigId) {
        QueryWrapper wrapper = Wrappers.query();
        wrapper.eq("project_id", projectId);
        wrapper.eq("metrics_table_config_id", metricsTableConfigId);
        MetricsTable metricsTable = super.getOne(wrapper);
        if (metricsTable == null) {
            MetricsTableConfig metricsTableConfig = metricsTableConfigService.getById(metricsTableConfigId);
            metricsTable = new MetricsTable();
            metricsTable.setProjectId(projectId);
            metricsTable.setMetricsTableConfigId(metricsTableConfigId);
            metricsTable.setTableName(metricsTableConfig.getTableName());
            metricsTable.setType(metricsTableConfig.getType());
            super.save(metricsTable);
        } else {
            loadRows(metricsTable);
        }
        return metricsTable;
    }

    private void loadRows(MetricsTable metricsTable) {
        List<MetricsRow> rows = metricsRowService.getByMetricesTableId(metricsTable.getId());
        metricsTable.setMetricsRows(rows);
        if (rows != null && !rows.isEmpty()) {
            rows.forEach(row -> {
                List<MetricsItem> items = metricsItemService.getByMetricesRowId(row.getId());
                row.setMetricsItems(items);
            });
        }
    }


    /**
     * 执行SQL查询
     *
     * @param sql
     * @return
     */
    public List<Map<String, Object>> executeSelect(String sql) {
        return getBaseMapper().executeSelect(sql);
    }

    public PlainResponse repair(String projectNo, Integer metricsTableConfigId) {

        MetricsTableConfig mtc = metricsTableConfigService.getFullById(metricsTableConfigId);
        MetricsTable metricsTable = createOrGetByProjectNoAndMetricsTableConfig(projectNo, mtc);
        String message = null;
        //根据度量表是迭代内还是项目内来分别补齐
        if (mtc.getPeriod().equals(MetricsTableConfig.PERIOD_ITERATION)) {
            List<IterationCycle> iterationCycles = iterationCycleService.listPastIteration(projectNo);
            int count = metricsRowService.reportIterationMetricsRows(metricsTable,metricsTable.getId(), metricsTable.getMetricsRows(), mtc.getMetricsItemConfigs(), iterationCycles);
            if (count == 0) {
                message = String.format("度量表“%s”迭代数据完整不需要补齐！", metricsTable.getTableName());
            } else {
                message = String.format("度量表“%s”迭代数据补齐完成，共补齐%d个迭代数据！", metricsTable.getTableName(), count);
            }
        } else {
            if (metricsTable.getMetricsRows() != null && !metricsTable.getMetricsRows().isEmpty()) {
                message = String.format("度量表“%s”数据完整不需要补齐！", metricsTable.getTableName());
            } else {
                metricsRowService.reportProjectMetricsRow(metricsTable,metricsTable.getId(), mtc.getMetricsItemConfigs());
                message = String.format("度量表“%s”数据补齐完成！", metricsTable.getTableName());
            }
        }
        return PlainResponse.ok(true).message(message);
    }

    /**
     * 根据项目ID和度量表配置ID创建或获取现有度量表（如果不存在则创建）
     *
     * @param projectNo
     * @param metricsTableConfig
     * @return
     */
    private MetricsTable createOrGetByProjectNoAndMetricsTableConfig(String projectNo, MetricsTableConfig metricsTableConfig) {
        QueryWrapper wrapper = Wrappers.query().eq("metrics_table_config_id", metricsTableConfig.getId()).eq("project_id", projectNo);
        MetricsTable metricsTable = getOne(wrapper);
        if (metricsTable == null) {
            metricsTable = new MetricsTable();
            metricsTable.setType(metricsTableConfig.getType());
            metricsTable.setTableName(metricsTableConfig.getTableName());
            metricsTable.setProjectId(projectNo);
            metricsTable.setMetricsTableConfigId(metricsTableConfig.getId());
            super.save(metricsTable);
        } else {
            loadRows(metricsTable);
        }
        return metricsTable;
    }

    public void updateTableNameByMetricsTableConfigId(Integer metricsTableConfigId, String tableName) {
        getBaseMapper().updateTableNameByMetricsTableConfigId(metricsTableConfigId, tableName);
    }
}
