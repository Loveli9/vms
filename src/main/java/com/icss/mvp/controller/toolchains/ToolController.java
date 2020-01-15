package com.icss.mvp.controller.toolchains;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.icss.mvp.entity.common.response.ListResponse;
import com.icss.mvp.entity.common.response.PageResponse;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.toolchains.Tool;
import com.icss.mvp.service.toolchains.ToolService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("toolchains/tool")
public class ToolController {

    @Autowired
    private ToolService service;

    @ResponseBody
    @RequestMapping(value = "/save", consumes = "application/json")
    public PlainResponse merge(@RequestBody Tool tool) {
        return service.merge(tool);
    }

    @ResponseBody
    @RequestMapping("/delete")
    public PlainResponse<Boolean> deleteById(Integer id) {
        boolean out = service.removeById(id);
        return PlainResponse.ok(out);
    }

    @ResponseBody
    @RequestMapping("/get_by_name")
    public ListResponse<Tool> getByMetricsTableConfigId(Page page, String name) {
        QueryWrapper<Tool> query = Wrappers.query();
        if (StringUtils.isNotEmpty(name)) {
            query.like("name", name);
        }
        IPage result = service.page(page, query);
        ListResponse response = PageResponse.ok(result.getRecords()).totalCount(result.getTotal()).pageNumber(result.getCurrent()).pageSize(result.getSize());
        return response;
    }

    @ResponseBody
    @RequestMapping("/get_by_id")
    public PlainResponse getById(Integer id) {
        return PlainResponse.ok(service.getById(id));
    }

}
