package org.apache.jsp.WEB_002dINF.views;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class login_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.Vector _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write(" \r\n");
      out.write("<!DOCTYPE html>\r\n");
      out.write("<html>\r\n");
      out.write("<head lang=\"en\">\r\n");
      out.write("    <meta charset=\"UTF-8\">\r\n");
      out.write("    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n");
      out.write("    <title>登录</title>\r\n");
      out.write("    ");
      out.write("\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("    <div class=\"login-main\">\r\n");
      out.write("        <div class=\"container\">\r\n");
      out.write("            <h1 class=\"login-title\">管理系统</h1>\r\n");
      out.write("        </div>\r\n");
      out.write("        <div class=\"login-content\">\r\n");
      out.write("            <div class=\"container\">\r\n");
      out.write("            <form class=\"form-signin\" role=\"form\" method=\"post\">\r\n");
      out.write("                   <div class=\"login-box\">\r\n");
      out.write("                       <ul class=\"login-list\">\r\n");
      out.write("                           <li><div class=\"input-box\"><input placeholder=\"用户名\" type=\"text\" name=\"username\" /><i class=\"icon-login-user\"></i></div></li>\r\n");
      out.write("                           <li><div class=\"input-box\"><input placeholder=\"密码\" type=\"password\" name=\"password\" /><i class=\"icon-login-password\"></i></div></li>\r\n");
      out.write("                           <li class=\"yzm-tool\"><input type=\"checkbox\" name=\"rememberMe\" />记住我</li>\r\n");
      out.write("                           <!--<li class=\"yzm-tool\"><div class=\"input-box\"><input placeholder=\"验证码\" type=\"text\" /></div><a class=\"yzm\" title=\"点击切换验证码\" href=\"\"><img src=\"images/yzm-pic.gif\" /></a></li>-->\r\n");
      out.write("                           <li>\r\n");
      out.write("                               <button class=\"login-btn\" type=\"submit\">登 录</button>\r\n");
      out.write("                               <!-- <a class=\"login-btn\" href=\"\">登 录</a> -->\r\n");
      out.write("                           </li>\r\n");
      out.write("                       </ul>\r\n");
      out.write("                   </div>\r\n");
      out.write("            </form>\r\n");
      out.write("            </div>\r\n");
      out.write("        </div>\r\n");
      out.write("        <p class=\"copyright\">\r\n");
      out.write("              版权所有\r\n");
      out.write("        </p>\r\n");
      out.write("    </div>\r\n");
      out.write("</body>\r\n");
      out.write("</html>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
