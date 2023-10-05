package com.intuit.monitor.service;

import com.intuit.common.entity.MonitorDTO;
import org.springframework.stereotype.Service;

@Service
public interface MonitorService {

    void monitorService(MonitorDTO monitorDTO);
}
