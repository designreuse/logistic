package com.frame.web.business.controller;

import com.frame.core.sql.RefRule;
import com.frame.web.business.entity.orgainzation.Dept;
import com.frame.web.business.entity.orgainzation.User;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class CtrlTest {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @GetMapping("/test")
    public String test() throws InterruptedException {
        String businessKey = "10086";
        identityService.setAuthenticatedUserId("2019");
//        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("duri", businessKey);
//        taskService.claim("428724a6-f0f4-11e9-9c27-780cb8d231d9","2019");
//        Thread.sleep(20000L);
        List<Task> tasks = taskService.createTaskQuery().active().list();


        return null;
    }

    public static void main(String[] args) {
        User user = new User();
        RefRule<User> rule = new RefRule<>(user, RefRule.Rule.INSERT);
        Dept dept = new Dept();
        RefRule<Dept> ruleDept = new RefRule<>(dept, RefRule.Rule.DELETE);
    }
}
