package com.icss.mvp.service.report;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.report.MetricsTableConfigDao;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.report.MetricsItemConfig;
import com.icss.mvp.entity.report.MetricsTable;
import com.icss.mvp.entity.report.MetricsTableConfig;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author chenjiabin
 * @createtime 2019-12-11
 */
@Service
@Transactional(rollbackFor = {Exception.class})
public class MetricsTableConfigService extends ServiceImpl<MetricsTableConfigDao, MetricsTableConfig> implements IService<MetricsTableConfig> {

    @Autowired
    private MetricsItemConfigService metricsItemConfigService;
    @Autowired
    private MetricsTableService metricsTableService;

    public PlainResponse merge(MetricsTableConfig metricsTableConfig) {
        //度量表配置信息检查
        PlainResponse checkResult = this.checkMetricsTableConfigInfo(metricsTableConfig);
        if (checkResult.getSucceed() == false) {
            return checkResult;
        }
        if (metricsTableConfig.getId() == null) {
            getBaseMapper().insert(metricsTableConfig);
        } else {
            List<MetricsItemConfig> metricsItemConfigs = metricsItemConfigService.getByMetricesTableConfigId(metricsTableConfig.getId());
            PlainResponse result = metricsItemConfigService.clearMetricsItemConfigs(metricsItemConfigs, metricsTableConfig.getMetricsItemConfigs());
            if (!result.getSucceed()) {
                return result;
            }
            getBaseMapper().updateById(metricsTableConfig);
            metricsTableService.updateTableNameByMetricsTableConfigId(metricsTableConfig.getId(), metricsTableConfig.getTableName());
        }
        if (metricsTableConfig.getMetricsItemConfigs() != null && !metricsTableConfig.getMetricsItemConfigs().isEmpty()) {
            for (MetricsItemConfig mic : metricsTableConfig.getMetricsItemConfigs()) {
                mic.setMetricsTableConfigId(metricsTableConfig.getId());
                metricsItemConfigService.saveOrUpdate(mic);
            }
        }
        return PlainResponse.ok(metricsTableConfig);
    }

    public MetricsTableConfig getFullById(Integer id) {
        MetricsTableConfig metricsTableConfig = super.getById(id);
        metricsTableConfig.setMetricsItemConfigs(metricsItemConfigService.getByMetricesTableConfigId(metricsTableConfig.getId()));
        return metricsTableConfig;
    }

    @Override
    public boolean removeById(Serializable id) {
        boolean val = super.removeById(id);
        metricsItemConfigService.deleteByMetricesTableConfigId(id);
        return val;
    }

    public PlainResponse checkMetricsTableConfigInfo(MetricsTableConfig metricsTableConfig) {
        if (checkFieldName(metricsTableConfig)) {
            return PlainResponse.fail("字段名只能是大、小写字母+下划线，不能有其它特殊字符。");
        }
        if (StringUtils.isEmpty(metricsTableConfig.getAlias())) {
            return PlainResponse.fail("度量表配置表“别名”不能为空。");
        }
        if (StringUtils.isEmpty(metricsTableConfig.getTableName())) {
            return PlainResponse.fail("度量表配置表“数据表名”不能为空。");
        }

        if (checkMetricsItemConfigAlias(metricsTableConfig)) {
            return PlainResponse.fail("度量项配置“别名”不能为空。");
        }
        List<String> alias = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<MetricsItemConfig> mics = metricsTableConfig.getMetricsItemConfigs();
        for (MetricsItemConfig mic : mics) {
            if (alias.contains(mic.getFieldAlias())) {
                return PlainResponse.fail("度量项“别名”不能重复：" + mic.getFieldAlias());
            } else {
                alias.add(mic.getFieldAlias());
            }
            if (names.contains(mic.getFieldName())) {
                return PlainResponse.fail("度量项“字段名”不能重复：" + mic.getFieldName());
            } else {
                names.add(mic.getFieldName());
            }
            boolean existsAlias = metricsItemConfigService.existsAlias(mic.getId(), mic.getFieldAlias());
            if (existsAlias) {
                return PlainResponse.fail("度量项“别名”不能重复：" + mic.getFieldAlias());
            }
            boolean existsName = metricsItemConfigService.existsName(mic.getId(), mic.getFieldName());
            if (existsName) {
                return PlainResponse.fail("度量项“字段名”不能重复：" + mic.getFieldAlias());
            }
        }
        return PlainResponse.ok(true);
        //检查通过
    }

    private boolean exists(Integer id, List<MetricsItemConfig> news) {
        for (MetricsItemConfig metricsItemConfig : news) {
            if (metricsItemConfig.getId() != null && metricsItemConfig.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    //度量项字段名检查
    private boolean checkFieldName(MetricsTableConfig metricsTableConfig) {
        if (metricsTableConfig.getMetricsItemConfigs() != null && !metricsTableConfig.getMetricsItemConfigs().isEmpty()) {
            for (MetricsItemConfig mic : metricsTableConfig.getMetricsItemConfigs()) {
                if (StringUtils.isEmpty(mic.getFieldName()) || !matchFieldName(mic.getFieldName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean matchFieldName(String fieldName) {
        Pattern p = Pattern.compile("[A-Za-z_]+");
        Matcher m = p.matcher(fieldName);
        return m.matches();
    }

    //度量项字段别名检查
    private boolean checkMetricsItemConfigAlias(MetricsTableConfig metricsTableConfig) {
        if (metricsTableConfig.getMetricsItemConfigs() != null && !metricsTableConfig.getMetricsItemConfigs().isEmpty()) {
            for (MetricsItemConfig mic : metricsTableConfig.getMetricsItemConfigs()) {
                if (StringUtils.isEmpty(mic.getFieldAlias())) {
                    return true;
                }
            }
        }
        return false;
    }

}
