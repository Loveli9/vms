package com.icss.mvp.controller.datacollection.config;

import com.icss.mvp.entity.datacollection.config.CollectionConfig;
import com.icss.mvp.service.datacollection.config.CollectionConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("datacollection/config/collection_config")
public class CollectionConfigController {
    @Autowired
    private CollectionConfigService collectionConfigService;

    @ResponseBody
    @RequestMapping("get_by_project_id")
    public CollectionConfig getByProjectId(String projectId) {
        CollectionConfig collectionConfig = collectionConfigService.getOrCreateFullByProjectId(projectId);
        return collectionConfig;
    }

    @ResponseBody
    @RequestMapping(value = "/saveCollectionConfig", consumes = "application/json")
    public CollectionConfig save(@RequestBody CollectionConfig collectionConfig) {
        Boolean saved = collectionConfigService.save(collectionConfig);
        if (saved){
            return collectionConfigService.getOrCreateFullByProjectId(collectionConfig.getProjectId());
        }
        return new CollectionConfig();
    }
}
