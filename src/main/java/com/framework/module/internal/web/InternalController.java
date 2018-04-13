package com.framework.module.internal.web;

import com.framework.module.internal.service.InternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class InternalController {
    private InternalService internalService;

    /**
     * 后台模板
     */
    @RequestMapping(value = "/internal/message/{mobile}/{message}",method = RequestMethod.GET)
    public ResponseEntity<?> adminTemplate(@PathVariable String mobile,
                                           @PathVariable String message,
                                           HttpServletRequest request) throws Exception {
        String pwd = request.getParameter("pwd");
        if(!"402881bc613110e701613".equals(pwd)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        internalService.sendMassage(mobile, message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Autowired
    public InternalController (InternalService internalService) {
        this.internalService = internalService;
    }
}
