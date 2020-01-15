package com.icss.mvp.service.datacollection.data;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.datacollection.data.ICollectionDataTableDao;
import com.icss.mvp.entity.datacollection.config.CollectionConfig;
import com.icss.mvp.entity.datacollection.data.CollectionDataTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CollectionDataTableService extends ServiceImpl<ICollectionDataTableDao, CollectionDataTable> implements IService<CollectionDataTable> {


    public List<CollectionDataTable> listFullByProjectId(String projectId) {
        return null;
    }
}
