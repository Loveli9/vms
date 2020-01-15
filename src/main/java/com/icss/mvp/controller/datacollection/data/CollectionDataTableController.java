package com.icss.mvp.controller.datacollection.data;

import com.icss.mvp.entity.datacollection.config.CollectionConfig;
import com.icss.mvp.entity.datacollection.data.CollectionDataTable;
import com.icss.mvp.service.datacollection.config.CollectionConfigService;
import com.icss.mvp.service.datacollection.data.CollectionDataTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("datacollection/data/collection_data_table")
public class CollectionDataTableController {
    @Autowired
    private CollectionDataTableService service;

    @ResponseBody
    @RequestMapping("get_by_project_id")
    public  List<CollectionDataTable>  getByProjectId(String projectId) {
        List<CollectionDataTable> tables = service.listFullByProjectId(projectId);
        return tables;
    }
}