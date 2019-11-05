package com.frame.core.bpmn;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;

public class BpmnUtils {
    private static ProcessEngine processEngine;
    /**
     * 单例模式获取引擎对象
     */
    public static ProcessEngine getProcessEngine() {
        if (processEngine == null) {
            /*
             * 使用默认的配置文件名称（camunda.cfg.xml）创建引擎对象
             */
            processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault().buildProcessEngine();
        }
        return processEngine;
    }
}
