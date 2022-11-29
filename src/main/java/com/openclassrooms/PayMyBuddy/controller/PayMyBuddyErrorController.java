package com.openclassrooms.PayMyBuddy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PayMyBuddyErrorController implements ErrorController {

    Logger logger = LoggerFactory.getLogger(PayMyBuddyErrorController.class);

    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        logger.info("An error occurred");

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                logger.info("Returning 404 error page");
                return "error-404";
            }
            if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                logger.info("Returning 500 error page");
                return "error-500";
            }
            if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                logger.info("Returning 401 error page");
                return "error-401";
            }
        }
        logger.info("Returning default error page");
        return "error";
    }
}
