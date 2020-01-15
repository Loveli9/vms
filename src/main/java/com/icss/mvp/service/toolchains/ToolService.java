package com.icss.mvp.service.toolchains;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.toolchains.ToolDao;
import com.icss.mvp.entity.common.response.PlainResponse;
import com.icss.mvp.entity.toolchains.Tool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Transactional
public class ToolService extends ServiceImpl<ToolDao, Tool> implements IService<Tool> {

    public PlainResponse merge(@RequestBody Tool tool) {

        if(tool.getId() == null) {
            PlainResponse checkResult = checkToolName(tool.getName());
            if (checkResult.getSucceed() == false) {
                return checkResult;
            }
            return PlainResponse.ok(save(tool));
        } else {
            String newName = tool.getName();
            String oldName = this.getById(tool.getId()).getName();
            if(!oldName.equalsIgnoreCase(newName)) {//修改了工具名
                PlainResponse checkResult = checkToolName(newName);
                if (checkResult.getSucceed() == false) {
                    return checkResult;
                }
            }
            return PlainResponse.ok(updateById(tool));
        }
    }

    private PlainResponse checkToolName(String name) {
        if(qryCountByName(name) > 0) {
            return PlainResponse.fail("工具名不能重复：" + name);
        }
        return PlainResponse.ok(true);
    }

    private int qryCountByName(String name) {
        QueryWrapper<Tool> query = Wrappers.query();
        query.eq("name", name);
        return count(query);
    }

}