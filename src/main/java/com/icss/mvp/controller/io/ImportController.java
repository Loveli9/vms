package com.icss.mvp.controller.io;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.icss.mvp.entity.common.response.BaseResponse;
import com.icss.mvp.service.io.ImportService;

/**
 * Created by Ray on 2018/8/9.
 */
@Controller
@RequestMapping("/import")
public class ImportController {

    @Autowired
    private ImportService importService;

    @RequestMapping("/projects")
    @ResponseBody
    public BaseResponse importProjects(@RequestParam(value = "file") MultipartFile file) {
        return importService.importProjects(file);
    }

    @RequestMapping("/keyRoles")
    @ResponseBody
    public BaseResponse importKeyRoles(@RequestParam(value = "file") MultipartFile file) {
        return importService.importKeyRoles(file);
    }

    @RequestMapping("/RDPM")
    @ResponseBody
    public BaseResponse importRDPM(@RequestParam(value = "file") MultipartFile file) {
        return importService.importRDPM(file);
    }

    @RequestMapping("/members")
    @ResponseBody
    public BaseResponse importMembers(@RequestParam(value = "file") MultipartFile file) {
        return importService.importMembers(file);
    }

    @RequestMapping("/OMP")
    @ResponseBody
    public BaseResponse importOMP(@RequestParam(value = "file") MultipartFile file) {
        return importService.importOMP(file);
    }

    @RequestMapping("/clock")
    @ResponseBody
    public BaseResponse importClock(@RequestParam(value = "file") MultipartFile file,
                                    @RequestParam(value = "time") String time) {
        return importService.importClock(file, time);
    }
    @RequestMapping("/import361MaturityProblem")
    @ResponseBody
    public BaseResponse import361MaturityProblem(@RequestParam(value = "file") MultipartFile file) {
        return importService.import361MaturityProblem(file);
    }
}
