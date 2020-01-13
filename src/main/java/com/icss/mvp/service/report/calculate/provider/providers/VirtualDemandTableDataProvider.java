package com.icss.mvp.service.report.calculate.provider.providers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("virtual_demand_table_data_provider")
@Transactional
public class VirtualDemandTableDataProvider extends VirtualPersonnelTableDataProvider {
}
