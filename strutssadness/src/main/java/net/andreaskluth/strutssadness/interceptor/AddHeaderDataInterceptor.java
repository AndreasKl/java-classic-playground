package net.andreaskluth.strutssadness.interceptor;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class AddHeaderDataInterceptor extends AbstractInterceptor {

  private static final long serialVersionUID = 1L;

  @Override
  public String intercept(ActionInvocation invocation) throws Exception {
    ActionContext.getContext().getValueStack().set("title", "There is a title.");
    return invocation.invoke();
  }

}
