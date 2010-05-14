package org.onebusaway.webapp.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.onebusaway.users.impl.authentication.AuthenticationResult;
import org.onebusaway.users.impl.authentication.LoginManager;
import org.onebusaway.users.services.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.ui.rememberme.TokenBasedRememberMeServices;

import com.opensymphony.xwork2.ActionSupport;

@Results( {@Result(type = "redirectAction", params = {
    "actionName", "/user/index"})})
public class LoginHandlerAction extends ActionSupport {

  private static final long serialVersionUID = 1L;

  private CurrentUserService _currentUserService;

  private TokenBasedRememberMeServices _rememberMeServices;

  @Autowired
  public void setCurrentUserService(CurrentUserService currentUserService) {
    _currentUserService = currentUserService;
  }
  
  @Autowired
  public void setRememberMeServices(TokenBasedRememberMeServices rememberMeServices) {
    _rememberMeServices = rememberMeServices;
  }

  @Override
  public String execute() {
    
    HttpServletRequest request = ServletActionContext.getRequest();
    HttpServletResponse response = ServletActionContext.getResponse();
    
    AuthenticationResult result = LoginManager.getResult(request);

    if (result == null)
      return INPUT;

    switch (result.getCode()) {
      case SUCCESS:
        
        _currentUserService.handleLogin(result.getProvider(),
            result.getIdentity(), result.getCredentials());
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if( authentication != null)
          _rememberMeServices.onLoginSuccess(request, response, authentication);

        return SUCCESS;
      case NO_SUCH_PROVIDER:
      case AUTHENTICATION_FAILED:
      default:
        return INPUT;
    }
  }
}
