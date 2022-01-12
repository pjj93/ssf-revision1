package ibf.ssf.ssftodo.controller;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import ibf.ssf.ssftodo.Constants;
import ibf.ssf.ssftodo.SsftodoApplication;
import ibf.ssf.ssftodo.service.TaskService;

@Controller
@RequestMapping(path="/task", produces=MediaType.TEXT_HTML_VALUE)
public class TaskController {
    private final Logger logger = Logger.getLogger(SsftodoApplication.class.getName());
 
    @Autowired
    private TaskService taskSvc;
    
    @PostMapping("save")
    public String postTaskSave(@RequestBody MultiValueMap<String, String> form) {
        String contents = form.getFirst("contents");

        logger.log(Level.INFO, "to be saved: '%s'".formatted(contents));
        
        taskSvc.save(Constants.TODO_KEY, contents);

        return "index";
    }

    @PostMapping
    public String Task(@RequestBody MultiValueMap<String, String> form, Model model) {
        String taskName = form.getFirst("taskName");
        String contents = form.getFirst("contents");
        
        List<String> tasks = new LinkedList<String>();
        if (null != contents && (contents.trim().length() > 0)) {
            //append new tasks to contents
            contents = "%s%s%s".formatted(contents, Constants.DELIMITER, taskName);
            tasks = Arrays.asList(contents.split(Constants.DELIMITER));
            // for (String t: contents.split("|"))
            //     tasks.add(t);
        } else {
            contents = taskName;
            tasks.add(contents);
        }
        
        logger.log(Level.INFO, "taskName: %s".formatted(taskName));
        logger.log(Level.INFO, "taskName: %s".formatted(contents));

        model.addAttribute("contents", contents);
        model.addAttribute("tasks", tasks);

        return "index";
    }
}
