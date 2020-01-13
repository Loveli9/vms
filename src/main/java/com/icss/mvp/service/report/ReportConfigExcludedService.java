package com.icss.mvp.service.report;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.report.IReportConfigExcludedDao;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.report.ReportConfigExcluded;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReportConfigExcludedService extends ServiceImpl<IReportConfigExcludedDao, ReportConfigExcluded> implements IService<ReportConfigExcluded> {

    //保存或更新裁剪列表
    public PlainResponse saveOrUpdateBatch(String projectNo, List<ReportConfigExcluded> excludeds) {
        if (excludeds != null && !excludeds.isEmpty()) {
            QueryWrapper condition = Wrappers.query().eq("project_id", projectNo);
            remove(condition);
            boolean result = super.saveOrUpdateBatch(excludeds);
            if (result) {
                return PlainResponse.ok(excludeds).message("项目裁剪结果保存成功！");
            } else {
                return PlainResponse.fail("项目裁剪结果保存失败!");
            }
        } else {
            QueryWrapper condition = Wrappers.query().eq("project_id", projectNo);
            remove(condition);
            return PlainResponse.ok(excludeds).message("项目裁剪结果保存成功！");
        }
    }

    //查询指定项目的裁剪列表
    public List<ReportConfigExcluded> listByProjectNo(String projectNo) {
        QueryWrapper condition = Wrappers.query().eq("project_id", projectNo);
        return list(condition);
    }  //查询指定项目的裁剪列表

    public List<ReportConfigExcluded> listByProjectNoAndReportConfigId(String projectNo, Integer reportConfigId) {
        QueryWrapper condition = Wrappers.query().eq("project_id", projectNo).eq("report_config_id", reportConfigId);
        return list(condition);
    }
}
