package com.openclassrooms.PayMyBuddy.controller;

import com.openclassrooms.PayMyBuddy.exception.EmailAlreadyExistsException;
import com.openclassrooms.PayMyBuddy.exception.IncorrectCurrentPasswordException;
import com.openclassrooms.PayMyBuddy.exception.PasswordAndConfirmationNotIdenticalException;
import com.openclassrooms.PayMyBuddy.exception.UserNotFoundException;
import com.openclassrooms.PayMyBuddy.model.PasswordUpdateDto;
import com.openclassrooms.PayMyBuddy.model.ProfileDto;
import com.openclassrooms.PayMyBuddy.model.UserDto;
import com.openclassrooms.PayMyBuddy.service.IUserService;
import com.openclassrooms.PayMyBuddy.utils.CurrentUserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private IUserService service;

    public static final String NEW_PROFILE_SUCCESS_MESSAGE = "\u2714 \u2007 Votre profil a été crée ! Vous pouvez maintenant vous " +
            "connecter.";
    public static final String UPDATE_PROFILE_SUCCESS_MESSAGE = "\u2714 \u2007 Votre profil a été mis à jour !";
    public static final String UPDATE_PASSWORD_SUCCESS_MESSAGE = "\u2714 \u2007 Votre mot de passe a été mis à jour !";

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/createProfile")
    public String createProfile(Model model) {
        if (CurrentUserUtils.getCurrentUserId() == null) {
            ProfileDto profileDto = new ProfileDto();
            model.addAttribute("profileDto", profileDto);
            return "createProfile";
        }
        return "redirect:/";
    }

    @PostMapping("/createProfile")
    public String saveNewProfile(@Valid ProfileDto profileDto, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            logger.info("Cannot create profile : invalid form");
            return "createProfile";
        }
        try {
            service.saveNewUser(profileDto);
            logger.info("New profile created");
            redirectAttributes.addFlashAttribute("message", NEW_PROFILE_SUCCESS_MESSAGE);
            return "redirect:/login";
        } catch (EmailAlreadyExistsException | PasswordAndConfirmationNotIdenticalException e) {
            logger.info("Cannot create profile : " + e.getMessage());
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            return "createProfile";
        }
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        model.addAttribute("userDto", service.getCurrentUserDto());
        model.addAttribute("passwordUpdateDto", new PasswordUpdateDto());
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid UserDto userDto, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            logger.info("Cannot update profile : invalid form");
            model.addAttribute("passwordUpdateDto", new PasswordUpdateDto());
            return "profile";
        }
        try {
            service.updateUser(userDto);
            logger.info("Profile updated");
            redirectAttributes.addFlashAttribute("message", UPDATE_PROFILE_SUCCESS_MESSAGE);
            return "redirect:/profile";
        } catch (EmailAlreadyExistsException | UserNotFoundException e) {
            logger.info("Cannot update profile : " + e.getMessage());
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            model.addAttribute("passwordUpdateDto", new PasswordUpdateDto());
            return "profile";
        }
    }

    @PostMapping("/password")
    public String updatePassword(@Valid PasswordUpdateDto passwordUpdateDto, BindingResult result, RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            logger.info("Cannot update password : invalid form");
            model.addAttribute("userDto", service.getCurrentUserDto());
            return "profile";
        }
        try {
            service.updatePassword(passwordUpdateDto);
            logger.info("Password updated");
            redirectAttributes.addFlashAttribute("message", UPDATE_PASSWORD_SUCCESS_MESSAGE);
            return "redirect:/profile";
        } catch (PasswordAndConfirmationNotIdenticalException | UserNotFoundException | IncorrectCurrentPasswordException e) {
            logger.info("Cannot update password : " + e.getMessage());
            model.addAttribute("userDto", service.getCurrentUserDto());
            ObjectError error = new ObjectError("globalError", e.getMessage());
            result.addError(error);
            return "profile";
        }
    }
}
