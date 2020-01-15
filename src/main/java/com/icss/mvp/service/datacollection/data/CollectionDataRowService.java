package com.icss.mvp.service.datacollection.data;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.icss.mvp.dao.datacollection.data.ICollectionDataRowDao;
import com.icss.mvp.entity.datacollection.data.CollectionDataRow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CollectionDataRowService extends ServiceImpl<ICollectionDataRowDao, CollectionDataRow> implements IService<CollectionDataRow> {

}
