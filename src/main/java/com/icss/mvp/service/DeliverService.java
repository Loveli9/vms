package com.icss.mvp.service;

import com.icss.mvp.dao.DeliverDao;
import com.icss.mvp.entity.Deliver;
import com.icss.mvp.entity.DeliverResult;
import com.icss.mvp.entity.TableSplitResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangjianbin
 * @date 2019/12/06
 */
@Service public class DeliverService {

    @Autowired DeliverDao deliverDao;

    public TableSplitResult queryList(TableSplitResult page, String sort, String sortOrder) {
        TableSplitResult data = new TableSplitResult();
        String zrAccount = deliverDao.queryAccount(page.getQueryMap().get("username").toString());
        page.getQueryMap().put("zrAccount", zrAccount);
        List<DeliverResult> resultList = deliverDao.queryList(page, sort, sortOrder);
        data.setRows(resultList);
        Integer total = deliverDao.queryListTotals(page);
        data.setTotal(total);
        return data;
    }

    public TableSplitResult queryAll(TableSplitResult page, String sort, String sortOrder) {
        TableSplitResult data = new TableSplitResult();
        List<DeliverResult> resultList = deliverDao.queryAll(page, sort, sortOrder);
        data.setRows(resultList);
        Integer total = deliverDao.queryAllTotals(page);
        data.setTotal(total);
        return data;
    }

    public void add(Deliver deliver) {
        deliverDao.add(deliver);

    }

    public Deliver queryEditPageInfo(String id) {
        return deliverDao.queryEditPageInfo(id);
    }

    public void update(Deliver deliver) {
        deliverDao.update(deliver);

    }

    public void delete(String[] ids) {
        for (String id : ids) {
            deliverDao.delete(id);
        }

    }
}
