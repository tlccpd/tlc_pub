package com.tlcpub.net.web.front.ctl;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.Request;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.tlcpub.net.core.exception.AlreadyExistException;
import com.tlcpub.net.core.exception.NotFoundException;
import com.tlcpub.net.core.util.MessageUtil;
import com.tlcpub.net.core.util.SessionUtil;
import com.tlcpub.net.net.usr.dto.User;
import com.tlcpub.net.net.usr.service.UserService;
import com.tlcpub.net.usr.type.Role;
import com.tlcpub.net.web.front.dto.LoginResult;


@Controller
public class UserController {


   @Autowired
   protected UserService userService;

   @Autowired
   protected Validator validator;

   Logger logger = Logger.getLogger(UserController.class);

  // --- LOGIN ---

   @RequestMapping(value="/login.html")
   public ModelAndView login(LoginResult result, Request request) {

      if(SessionUtil.isLogined(request))
         return new ModelAndView("main");

      if(result.getResult() != null){
         MessageUtil.saveMessage(request, "authentication."+result.getResult());
      }

      return new ModelAndView("login.form");
   }

  // --- MEMBER SUBSCRIBE ---

   @RequestMapping(value="/user/subscribe.html", method=RequestMethod.GET)
   public ModelAndView subscribeForm(Request request) {

      if(SessionUtil.isLogined(request))
         return new ModelAndView("main");

      return new ModelAndView("user.subscribe.form");
   }

   @RequestMapping(value="/user/subscribe.html", method=RequestMethod.POST)
   public ModelAndView subscribeSubmit(User user, BindingResult result, Request request) {

      if(SessionUtil.isLogined(request))
         return new ModelAndView("main");

      user.setRole(Role.MEMBER);

//      user.fillNullPropWithDefaultValue();
      validator.validate(user, result);
      if (result.hasErrors()){
         return new ModelAndView("user.subscribe.form");
      }
//      user.recoverToPreviousState();

      ModelAndView mav = null;
      try{
         userService.subscribe(user);
         mav = new ModelAndView("user.subscribe.result");
      }catch(AlreadyExistException aee){
         result.reject("alreadyExist.user", new Object[]{user.getId()}, "Requested user id is already exist");
         return new ModelAndView("user.subscribe.form");
      }
      return mav;
   }


  // --- PROFILE ---

   @RequestMapping(value="/user/profile.html")
   public ModelAndView profileView(Request request) {

      UserDetails userDetails = SessionUtil.getUserDetails(request);
      if(userDetails == null)
         return new ModelAndView("login.form");

      ModelAndView mav = null;
      try{
         User user = userService.getProfile(userDetails.getUsername());
         mav = new ModelAndView("user.profile.view", "user", user);
      }catch(NotFoundException nee){
         logger.fatal("Logined user's profile is not found - "+userDetails.getUsername());
         SessionUtil.invalidate(request);
         mav = new ModelAndView("login.form");
      }
      return mav;
   }


  // --- MANAGER REGISTERATION ---

   @RequestMapping(value="/user/manager/list.html")
   public ModelAndView managerList(HttpServletRequest request){
      List<User> managerList = userService.getEntireManagerProfiles();
      return new ModelAndView("user.manager.list", "managerList", managerList);
   }

   @RequestMapping(value="/user/manager/register.html", method=RequestMethod.GET)
   public ModelAndView managerRegisterForm(HttpServletRequest request) {
      User user = new User();
      user.setRole(Role.OPERATOR);
      return new ModelAndView("user.manager.register.form", "manager", user);
   }

   @RequestMapping(value="/user/manager/register.html", method=RequestMethod.POST)
   public ModelAndView managerRegisterSubmit(@ModelAttribute("manager") User user, BindingResult result, HttpServletRequest request) {

      validator.validate(user, result);
      if (result.hasErrors()){
         return new ModelAndView("user.manager.register.form");
      }

      try{
         userService.subscribe(user);
      }catch(AlreadyExistException maee){
         result.reject("alreadyExist.manager", new Object[]{user.getId()}, "Requested user's id is already exist");
         return new ModelAndView("user.manager.register.form");
      }
      return managerList(request);
   }
}