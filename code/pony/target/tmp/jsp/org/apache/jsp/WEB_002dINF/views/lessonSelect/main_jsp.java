package org.apache.jsp.WEB_002dINF.views.lessonSelect;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class main_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.Vector _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_s_url_value_nobody;
  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_c_forEach_var_items;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_s_url_value_nobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _jspx_tagPool_c_forEach_var_items = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_s_url_value_nobody.release();
    _jspx_tagPool_c_forEach_var_items.release();
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
      out.write("<!DOCTYPE html>\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n");
      out.write("<title>选课管理</title>\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
      if (_jspx_meth_s_url_0(_jspx_page_context))
        return;
      out.write("\" />\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
      if (_jspx_meth_s_url_1(_jspx_page_context))
        return;
      out.write("\" />\r\n");
      out.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"");
      if (_jspx_meth_s_url_2(_jspx_page_context))
        return;
      out.write("\" />\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      if (_jspx_meth_s_url_3(_jspx_page_context))
        return;
      out.write("\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      if (_jspx_meth_s_url_4(_jspx_page_context))
        return;
      out.write("\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      if (_jspx_meth_s_url_5(_jspx_page_context))
        return;
      out.write("\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      if (_jspx_meth_s_url_6(_jspx_page_context))
        return;
      out.write("\"></script>\r\n");
      out.write("</head>\r\n");
      out.write("<body class=\"easyui-layout\">\r\n");
      out.write("<div class=\"easyui-layout\" data-options=\"fit:true\">\r\n");
      out.write("    <!-- Begin of toolbar -->\r\n");
      out.write("    <div id=\"my-toolbar-2\">\r\n");
      out.write("        <div class=\"my-toolbar-button\">\r\n");
      out.write("            <a href=\"#\" class=\"easyui-linkbutton\" iconCls=\"icon-add\" onclick=\"openAdd()\" plain=\"true\">选课</a>\r\n");
      out.write("            <a href=\"#\" class=\"easyui-linkbutton\" iconCls=\"icon-remove\" onclick=\"removeItem()\" plain=\"true\">删除</a>\r\n");
      out.write("        </div>\r\n");
      out.write("        <div class=\"my-toolbar-search\">\r\n");
      out.write("            <label>年级：</label> \r\n");
      out.write("            <select id=\"gradeSelect\" name=\"grade\" class=\"my-select\" panelHeight=\"auto\" style=\"width:100px\">\r\n");
      out.write("                <option value=\"\">请选择</option>\r\n");
      out.write("                ");
      if (_jspx_meth_c_forEach_0(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("            </select>\r\n");
      out.write("            <label>班级：</label> \r\n");
      out.write("            <select name=\"schoolClass\" class=\"my-select\" panelHeight=\"auto\" style=\"width:100px\">\r\n");
      out.write("            </select>\r\n");
      out.write("            <label>学生：</label> \r\n");
      out.write("            <select name=\"student\" class=\"my-select\" panelHeight=\"auto\" style=\"width:100px\">\r\n");
      out.write("            </select>\r\n");
      out.write("            <a href=\"#\" id=\"searchButton\" class=\"easyui-linkbutton\" iconCls=\"icon-search\">查询</a>\r\n");
      out.write("        </div>\r\n");
      out.write("    </div>\r\n");
      out.write("    <!-- End of toolbar -->\r\n");
      out.write("    <table id=\"my-datagrid-2\" class=\"easyui-datagrid\" toolbar=\"#my-toolbar-2\">\r\n");
      out.write("    \t <thead> \r\n");
      out.write("            <tr> \r\n");
      out.write("                <th data-options=\"field:'arrangeId',width:100\">ID</th> \r\n");
      out.write("                <th data-options=\"field:'subjectName',width:100\">科目</th> \r\n");
      out.write("                <th data-options=\"field:'teacherName',width:100\">老师</th> \r\n");
      out.write("                <th data-options=\"field:'classroom',width:100\">上课地点</th> \r\n");
      out.write("                <th data-options=\"field:'period',width:100\">课时</th> \r\n");
      out.write("                <th data-options=\"field:'credit',width:100\">学分</th> \r\n");
      out.write("                <th data-options=\"field:'timesStr',width:100\">时间安排</th> \r\n");
      out.write("            </tr> \r\n");
      out.write("        </thead> \r\n");
      out.write("    </table>\r\n");
      out.write("</div>\r\n");
      out.write("<!-- Begin of easyui-dialog -->\r\n");
      out.write("<div id=\"my-dialog-2\" class=\"easyui-dialog\" data-options=\"closed:true,iconCls:'icon-save'\" style=\"width:600px;height:400px; padding:10px;\">\r\n");
      out.write("\t<table id=\"my-datagrid-3\" class=\"easyui-datagrid\"></table>\r\n");
      out.write("</div>\r\n");
      out.write("<!-- End of easyui-dialog -->\r\n");
      out.write("<script type=\"text/javascript\" src=\"");
      if (_jspx_meth_s_url_7(_jspx_page_context))
        return;
      out.write("\"></script>\r\n");
      out.write("<script type=\"text/javascript\">\r\n");
      out.write("\tvar selectStudentId;//保存当前选中学生ID\r\n");
      out.write("\t$(document).ready(function(){\r\n");
      out.write("\t\t$(\"#gradeSelect\").change(function(){\r\n");
      out.write("\t\t\tvar grade=$(this).children('option:selected').val();\r\n");
      out.write("\t\t\tif(grade == \"\"){\r\n");
      out.write("\t\t\t\t$(\"select[name='schoolClass']\").empty();\r\n");
      out.write("\t\t\t\t$(\"select[name='student']\").empty();\r\n");
      out.write("\t\t\t}else{\r\n");
      out.write("\t\t\t\t$.ajax({\r\n");
      out.write("\t\t\t\t\turl:\"");
      if (_jspx_meth_s_url_8(_jspx_page_context))
        return;
      out.write("\",\r\n");
      out.write("\t\t\t\t\tdata:{gradeId: grade},\r\n");
      out.write("\t\t\t\t\tmethod:\"GET\",\r\n");
      out.write("\t\t\t\t\tdataType:\"json\",\r\n");
      out.write("\t\t\t\t\tsuccess:function(data){\r\n");
      out.write("\t\t\t\t\t\t$(\"select[name='schoolClass']\").empty();\r\n");
      out.write("\t\t\t\t\t\t$(\"select[name='schoolClass']\").append(\"<option value=''>请选择</option>\");\r\n");
      out.write("\t\t\t\t\t\tvar len=data.length;\r\n");
      out.write("\t\t\t\t\t\tfor(var i=0;i<len;i++){\r\n");
      out.write("\t\t\t\t\t\t\tvar item=data[i];\r\n");
      out.write("\t\t\t\t\t\t\t$(\"select[name='schoolClass']\").append(\"<option value='\"+item.classId+\"'>\"+item.name+\"</option>\");\r\n");
      out.write("\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t}\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t});\r\n");
      out.write("\t\t$(\"select[name='schoolClass']\").change(function(){\r\n");
      out.write("\t\t\tvar schoolClass=$(this).children('option:selected').val();\r\n");
      out.write("\t\t\tif(schoolClass == \"\"){\r\n");
      out.write("\t\t\t\t$(\"select[name='student']\").empty();\r\n");
      out.write("\t\t\t}else{\r\n");
      out.write("\t\t\t\t$.ajax({\r\n");
      out.write("\t\t\t\t\turl:\"");
      if (_jspx_meth_s_url_9(_jspx_page_context))
        return;
      out.write("\",\r\n");
      out.write("\t\t\t\t\tdata:{classId: schoolClass},\r\n");
      out.write("\t\t\t\t\tmethod:\"GET\",\r\n");
      out.write("\t\t\t\t\tdataType:\"json\",\r\n");
      out.write("\t\t\t\t\tsuccess:function(data){\r\n");
      out.write("\t\t\t\t\t\t$(\"select[name='student']\").empty();\r\n");
      out.write("\t\t\t\t\t\t$(\"select[name='student']\").append(\"<option value=''>请选择</option>\");\r\n");
      out.write("\t\t\t\t\t\tvar len=data.length;\r\n");
      out.write("\t\t\t\t\t\tfor(var i=0;i<len;i++){\r\n");
      out.write("\t\t\t\t\t\t\tvar item=data[i];\r\n");
      out.write("\t\t\t\t\t\t\t$(\"select[name='student']\").append(\"<option value='\"+item.studentId+\"'>\"+item.name+\"</option>\");\r\n");
      out.write("\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t}\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t});\r\n");
      out.write("\t});\r\n");
      out.write("\t/**\r\n");
      out.write("\t* Name 添加记录\r\n");
      out.write("\t*/\r\n");
      out.write("\tfunction add(){\r\n");
      out.write("\t\t$('#my-form-2').form('submit', {\r\n");
      out.write("\t\t\turl:\"");
      if (_jspx_meth_s_url_10(_jspx_page_context))
        return;
      out.write("\",\r\n");
      out.write("\t\t\tsuccess:function(data){\r\n");
      out.write("\t\t\t\tif(data){\r\n");
      out.write("\t\t\t\t\t$.messager.alert('信息提示','提交成功！','info');\r\n");
      out.write("\t\t\t\t\t$(\"#searchButton\").click();\r\n");
      out.write("\t\t\t\t\t$('#my-dialog-2').dialog('close');\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t\telse\r\n");
      out.write("\t\t\t\t{\r\n");
      out.write("\t\t\t\t\t$.messager.alert('信息提示','提交失败！','info');\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t});\r\n");
      out.write("\t}\r\n");
      out.write("\t\r\n");
      out.write("\t/**\r\n");
      out.write("\t* Name 修改记录\r\n");
      out.write("\t*/\r\n");
      out.write("\tfunction edit(){\r\n");
      out.write("\t\t$('#my-form-2').form('submit', {\r\n");
      out.write("\t\t\turl:\"");
      if (_jspx_meth_s_url_11(_jspx_page_context))
        return;
      out.write("\",\r\n");
      out.write("\t\t\tsuccess:function(data){\r\n");
      out.write("\t\t\t\tif(data){\r\n");
      out.write("\t\t\t\t\t$.messager.alert('信息提示','提交成功！','info');\r\n");
      out.write("\t\t\t\t\t$('#my-dialog-2').dialog('close');\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t\telse\r\n");
      out.write("\t\t\t\t{\r\n");
      out.write("\t\t\t\t\t$.messager.alert('信息提示','提交失败！','info');\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t});\r\n");
      out.write("\t}\r\n");
      out.write("\t\r\n");
      out.write("\t/**\r\n");
      out.write("\t* Name 删除记录\r\n");
      out.write("\t*/\r\n");
      out.write("\tfunction removeItem(){\r\n");
      out.write("\t\t$.messager.confirm('信息提示','确定要删除该记录？', function(result){\r\n");
      out.write("\t\t\tif(result){\r\n");
      out.write("\t\t\t\tvar item = $('#my-datagrid-2').datagrid('getSelected');\r\n");
      out.write("\t\t\t\t$.ajax({\r\n");
      out.write("\t\t\t\t\turl:\"");
      if (_jspx_meth_s_url_12(_jspx_page_context))
        return;
      out.write("\",\r\n");
      out.write("\t\t\t\t\tdata:{studentId:selectStudentId, arrangeId: item.arrangeId},\r\n");
      out.write("\t\t\t\t\tmethod:\"POST\",\r\n");
      out.write("\t\t\t\t\tdataType:\"json\",\r\n");
      out.write("\t\t\t\t\tsuccess:function(data){\r\n");
      out.write("\t\t\t\t\t\tif(data){\r\n");
      out.write("\t\t\t\t\t\t\t$.messager.alert('信息提示','删除成功！','info');\t\t\r\n");
      out.write("\t\t\t\t\t\t\t$('#my-datagrid-2').datagrid({\r\n");
      out.write("\t\t\t\t\t\t\t\tdata: data\r\n");
      out.write("\t\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t\telse\r\n");
      out.write("\t\t\t\t\t\t{\r\n");
      out.write("\t\t\t\t\t\t\t$.messager.alert('信息提示','删除失败！','info');\t\t\r\n");
      out.write("\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t}\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t}\t\r\n");
      out.write("\t\t});\r\n");
      out.write("\t}\r\n");
      out.write("\t\r\n");
      out.write("\t/**\r\n");
      out.write("\t* Name 打开添加窗口\r\n");
      out.write("\t*/\r\n");
      out.write("\tfunction openAdd(){\r\n");
      out.write("\t\tvar studentId=$(\"select[name='student']\").children('option:selected').val();\r\n");
      out.write("\t\tif(studentId==null || studentId == \"\"){\r\n");
      out.write("\t\t\talert(\"请选择学生\");\r\n");
      out.write("\t\t\treturn;\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\t$('#my-datagrid-3').datagrid({\r\n");
      out.write("\t\t\turl:\"");
      if (_jspx_meth_s_url_13(_jspx_page_context))
        return;
      out.write("\",\r\n");
      out.write("\t\t\tqueryParams:{studentId: studentId},\r\n");
      out.write("\t\t\tmethod:'get',\r\n");
      out.write("\t\t\trownumbers:true,\r\n");
      out.write("\t\t\tsingleSelect:true,\r\n");
      out.write("\t\t\tmultiSort:true,\r\n");
      out.write("\t\t\tfitColumns:true,\r\n");
      out.write("\t\t\tfit:true,\r\n");
      out.write("\t\t\tcolumns:[[\r\n");
      out.write("\t\t\t\t{ field:'arrangeId',title:'ID',width:100,sortable:true},\r\n");
      out.write("\t\t\t\t{ field:'subjectName',title:'科目',width:180,sortable:true},\r\n");
      out.write("\t\t\t\t{ field:'teacherName',title:'老师',width:180,sortable:true},\r\n");
      out.write("\t\t\t\t{ field:'classroom',title:'上课地点',width:180,sortable:true},\r\n");
      out.write("\t\t\t\t{ field:'period',title:'课时',width:180,sortable:true},\r\n");
      out.write("\t\t\t\t{ field:'credit',title:'学分',width:180,sortable:true},\r\n");
      out.write("\t\t\t\t{ field:'gradesStr',title:'可选年级',width:180,sortable:true},\r\n");
      out.write("\t\t\t\t{ field:'timesStr',title:'时间安排',width:180,sortable:true}\r\n");
      out.write("\t\t\t]],\r\n");
      out.write("\t\t\tonDblClickRow:function(rowIndex, rowData){\r\n");
      out.write("\t\t\t\t$.ajax({\r\n");
      out.write("\t\t\t\t\turl:\"");
      if (_jspx_meth_s_url_14(_jspx_page_context))
        return;
      out.write("\",\r\n");
      out.write("\t\t\t\t\tdata:{studentId: studentId, arrangeId: rowData.arrangeId},\r\n");
      out.write("\t\t\t\t\tmethod:\"POST\",\r\n");
      out.write("\t\t\t\t\tdataType:\"json\",\r\n");
      out.write("\t\t\t\t\tsuccess:function(data){\r\n");
      out.write("\t\t\t\t\t\tif(data){\r\n");
      out.write("\t\t\t\t\t\t\t$('#my-datagrid-2').datagrid({\r\n");
      out.write("\t\t\t\t\t\t\t\tdata: data\r\n");
      out.write("\t\t\t\t\t\t\t});\r\n");
      out.write("\t\t\t\t\t\t\t$.messager.alert('信息提示','选课成功！','info');\t\t\r\n");
      out.write("\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t\telse\r\n");
      out.write("\t\t\t\t\t\t{\r\n");
      out.write("\t\t\t\t\t\t\t$.messager.alert('信息提示','选课失败！','info');\t\t\r\n");
      out.write("\t\t\t\t\t\t}\r\n");
      out.write("\t\t\t\t\t}\t\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t\t$('#my-dialog-2').dialog('close');\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t});\r\n");
      out.write("\t\t$('#my-dialog-2').dialog({\r\n");
      out.write("\t\t\tclosed: false,\r\n");
      out.write("\t\t\tmodal:true,\r\n");
      out.write("            title: \"添加信息\",\r\n");
      out.write("            buttons: [{\r\n");
      out.write("                text: '确定',\r\n");
      out.write("                iconCls: 'icon-ok',\r\n");
      out.write("                handler: add\r\n");
      out.write("            }, {\r\n");
      out.write("                text: '取消',\r\n");
      out.write("                iconCls: 'icon-cancel',\r\n");
      out.write("                handler: function () {\r\n");
      out.write("                    $('#my-dialog-2').dialog('close');                    \r\n");
      out.write("                }\r\n");
      out.write("            }]\r\n");
      out.write("        });\r\n");
      out.write("\t}\r\n");
      out.write("\t\r\n");
      out.write("\t/**\r\n");
      out.write("\t* Name 打开修改窗口\r\n");
      out.write("\t*/\r\n");
      out.write("\tfunction openEdit(){\r\n");
      out.write("\t\t$('#my-form-2').form('clear');\r\n");
      out.write("\t\tvar item = $('#my-datagrid-2').datagrid('getSelected');\r\n");
      out.write("\t\t//alert(item.productid);return;\r\n");
      out.write("\t\t$.ajax({\r\n");
      out.write("\t\t\turl:\"");
      if (_jspx_meth_s_url_15(_jspx_page_context))
        return;
      out.write("\",\r\n");
      out.write("\t\t\tdata:{id: item.gradeId},\r\n");
      out.write("\t\t\tdataType:'json',\r\n");
      out.write("\t\t\tsuccess:function(data){\r\n");
      out.write("\t\t\t\tif(data){\r\n");
      out.write("\t\t\t\t\t//绑定值\r\n");
      out.write("\t\t\t\t\t$('#my-form-2').form('load', data);\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t\telse{\r\n");
      out.write("\t\t\t\t\t$('#my-dialog-2').dialog('close');\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t}\t\r\n");
      out.write("\t\t});\r\n");
      out.write("\t\t$('#my-dialog-2').dialog({\r\n");
      out.write("\t\t\tclosed: false,\r\n");
      out.write("\t\t\tmodal:true,\r\n");
      out.write("            title: \"修改信息\",\r\n");
      out.write("            buttons: [{\r\n");
      out.write("                text: '确定',\r\n");
      out.write("                iconCls: 'icon-ok',\r\n");
      out.write("                handler: edit\r\n");
      out.write("            }, {\r\n");
      out.write("                text: '取消',\r\n");
      out.write("                iconCls: 'icon-cancel',\r\n");
      out.write("                handler: function () {\r\n");
      out.write("                    $('#my-dialog-2').dialog('close');                    \r\n");
      out.write("                }\r\n");
      out.write("            }]\r\n");
      out.write("        });\r\n");
      out.write("\t}\t\r\n");
      out.write("\t\r\n");
      out.write("\tfunction reload(){\r\n");
      out.write("\t\t$('#my-datagrid-2').datagrid('reload');\r\n");
      out.write("\t}\r\n");
      out.write("\t\r\n");
      out.write("\t$(\"#searchButton\").click(function(){\r\n");
      out.write("\t\tvar studentId=$(\"select[name='student']\").children('option:selected').val();\r\n");
      out.write("\t\tif(studentId==null || studentId == \"\"){\r\n");
      out.write("\t\t\talert(\"请选择学生\");\r\n");
      out.write("\t\t\treturn;\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\tselectStudentId=studentId;\r\n");
      out.write("\t\t$.ajax({\r\n");
      out.write("\t\t\turl:\"");
      if (_jspx_meth_s_url_16(_jspx_page_context))
        return;
      out.write("\",\r\n");
      out.write("\t\t\tdata:{studentId: studentId},\r\n");
      out.write("\t\t\tmethod:\"GET\",\r\n");
      out.write("\t\t\tdataType:\"json\",\r\n");
      out.write("\t\t\tsuccess:function(data){\r\n");
      out.write("\t\t\t\t$('#my-datagrid-2').datagrid({\r\n");
      out.write("\t\t\t\t\tdata: data\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t\t}\t\r\n");
      out.write("\t\t});\r\n");
      out.write("\t\t\r\n");
      out.write("\t});\r\n");
      out.write("\t\r\n");
      out.write("\tfunction saveResult(){\r\n");
      out.write("\t\tvar cell=$(\"#my-datagrid-2\").datagrid(\"cell\"); \r\n");
      out.write("\t\t$(\"#my-datagrid-2\").datagrid(\"endEdit\",cell.index); \r\n");
      out.write("\t\tvar updated = $(\"#my-datagrid-2\").datagrid('getChanges', \"updated\"); \r\n");
      out.write("\t\tif(updated.length>0){ \r\n");
      out.write("\t\t\t/* alert(JSON.stringify(updated)); \r\n");
      out.write("\t\t\tfor(var u in updated){ \r\n");
      out.write("\t\t\t\talert(updated[u].studentId+\":\"+updated[u].score); \r\n");
      out.write("\t\t\t}  */\r\n");
      out.write("\t\t\t$.ajax({\r\n");
      out.write("\t\t\t\theaders: {\r\n");
      out.write("\t                'Accept': 'application/json',\r\n");
      out.write("\t                'Content-Type': 'application/json'\r\n");
      out.write("\t            },\r\n");
      out.write("\t\t\t\turl:\"");
      if (_jspx_meth_s_url_17(_jspx_page_context))
        return;
      out.write("\",\r\n");
      out.write("\t\t\t\tdata:JSON.stringify(updated),\r\n");
      out.write("\t\t\t\ttype:\"POST\",\r\n");
      out.write("\t\t\t\tdataType:'json',\r\n");
      out.write("\t\t\t\tsuccess:function(data){\r\n");
      out.write("\t\t\t\t\talert(data);\r\n");
      out.write("\t\t\t\t}\t\r\n");
      out.write("\t\t\t});\r\n");
      out.write("\t\t}\r\n");
      out.write("\t}\r\n");
      out.write("\t\r\n");
      out.write("</script>\r\n");
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

  private boolean _jspx_meth_s_url_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.springframework.web.servlet.tags.UrlTag _jspx_th_s_url_0 = (org.springframework.web.servlet.tags.UrlTag) _jspx_tagPool_s_url_value_nobody.get(org.springframework.web.servlet.tags.UrlTag.class);
    _jspx_th_s_url_0.setPageContext(_jspx_page_context);
    _jspx_th_s_url_0.setParent(null);
    _jspx_th_s_url_0.setValue("/static/easyui/themes/default/easyui.css");
    int[] _jspx_push_body_count_s_url_0 = new int[] { 0 };
    try {
      int _jspx_eval_s_url_0 = _jspx_th_s_url_0.doStartTag();
      if (_jspx_th_s_url_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_url_0[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_url_0.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_url_0.doFinally();
      _jspx_tagPool_s_url_value_nobody.reuse(_jspx_th_s_url_0);
    }
    return false;
  }

  private boolean _jspx_meth_s_url_1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.springframework.web.servlet.tags.UrlTag _jspx_th_s_url_1 = (org.springframework.web.servlet.tags.UrlTag) _jspx_tagPool_s_url_value_nobody.get(org.springframework.web.servlet.tags.UrlTag.class);
    _jspx_th_s_url_1.setPageContext(_jspx_page_context);
    _jspx_th_s_url_1.setParent(null);
    _jspx_th_s_url_1.setValue("/static/css/style.css");
    int[] _jspx_push_body_count_s_url_1 = new int[] { 0 };
    try {
      int _jspx_eval_s_url_1 = _jspx_th_s_url_1.doStartTag();
      if (_jspx_th_s_url_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_url_1[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_url_1.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_url_1.doFinally();
      _jspx_tagPool_s_url_value_nobody.reuse(_jspx_th_s_url_1);
    }
    return false;
  }

  private boolean _jspx_meth_s_url_2(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.springframework.web.servlet.tags.UrlTag _jspx_th_s_url_2 = (org.springframework.web.servlet.tags.UrlTag) _jspx_tagPool_s_url_value_nobody.get(org.springframework.web.servlet.tags.UrlTag.class);
    _jspx_th_s_url_2.setPageContext(_jspx_page_context);
    _jspx_th_s_url_2.setParent(null);
    _jspx_th_s_url_2.setValue("/static/css/icon.css");
    int[] _jspx_push_body_count_s_url_2 = new int[] { 0 };
    try {
      int _jspx_eval_s_url_2 = _jspx_th_s_url_2.doStartTag();
      if (_jspx_th_s_url_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_url_2[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_url_2.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_url_2.doFinally();
      _jspx_tagPool_s_url_value_nobody.reuse(_jspx_th_s_url_2);
    }
    return false;
  }

  private boolean _jspx_meth_s_url_3(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.springframework.web.servlet.tags.UrlTag _jspx_th_s_url_3 = (org.springframework.web.servlet.tags.UrlTag) _jspx_tagPool_s_url_value_nobody.get(org.springframework.web.servlet.tags.UrlTag.class);
    _jspx_th_s_url_3.setPageContext(_jspx_page_context);
    _jspx_th_s_url_3.setParent(null);
    _jspx_th_s_url_3.setValue("/static/js/jquery.min.js");
    int[] _jspx_push_body_count_s_url_3 = new int[] { 0 };
    try {
      int _jspx_eval_s_url_3 = _jspx_th_s_url_3.doStartTag();
      if (_jspx_th_s_url_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_url_3[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_url_3.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_url_3.doFinally();
      _jspx_tagPool_s_url_value_nobody.reuse(_jspx_th_s_url_3);
    }
    return false;
  }

  private boolean _jspx_meth_s_url_4(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.springframework.web.servlet.tags.UrlTag _jspx_th_s_url_4 = (org.springframework.web.servlet.tags.UrlTag) _jspx_tagPool_s_url_value_nobody.get(org.springframework.web.servlet.tags.UrlTag.class);
    _jspx_th_s_url_4.setPageContext(_jspx_page_context);
    _jspx_th_s_url_4.setParent(null);
    _jspx_th_s_url_4.setValue("/static/easyui/jquery.easyui.min.js");
    int[] _jspx_push_body_count_s_url_4 = new int[] { 0 };
    try {
      int _jspx_eval_s_url_4 = _jspx_th_s_url_4.doStartTag();
      if (_jspx_th_s_url_4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_url_4[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_url_4.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_url_4.doFinally();
      _jspx_tagPool_s_url_value_nobody.reuse(_jspx_th_s_url_4);
    }
    return false;
  }

  private boolean _jspx_meth_s_url_5(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.springframework.web.servlet.tags.UrlTag _jspx_th_s_url_5 = (org.springframework.web.servlet.tags.UrlTag) _jspx_tagPool_s_url_value_nobody.get(org.springframework.web.servlet.tags.UrlTag.class);
    _jspx_th_s_url_5.setPageContext(_jspx_page_context);
    _jspx_th_s_url_5.setParent(null);
    _jspx_th_s_url_5.setValue("/static/easyui/locale/easyui-lang-zh_CN.js");
    int[] _jspx_push_body_count_s_url_5 = new int[] { 0 };
    try {
      int _jspx_eval_s_url_5 = _jspx_th_s_url_5.doStartTag();
      if (_jspx_th_s_url_5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_url_5[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_url_5.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_url_5.doFinally();
      _jspx_tagPool_s_url_value_nobody.reuse(_jspx_th_s_url_5);
    }
    return false;
  }

  private boolean _jspx_meth_s_url_6(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.springframework.web.servlet.tags.UrlTag _jspx_th_s_url_6 = (org.springframework.web.servlet.tags.UrlTag) _jspx_tagPool_s_url_value_nobody.get(org.springframework.web.servlet.tags.UrlTag.class);
    _jspx_th_s_url_6.setPageContext(_jspx_page_context);
    _jspx_th_s_url_6.setParent(null);
    _jspx_th_s_url_6.setValue("/static/easyui/dateFormat.js");
    int[] _jspx_push_body_count_s_url_6 = new int[] { 0 };
    try {
      int _jspx_eval_s_url_6 = _jspx_th_s_url_6.doStartTag();
      if (_jspx_th_s_url_6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_url_6[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_url_6.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_url_6.doFinally();
      _jspx_tagPool_s_url_value_nobody.reuse(_jspx_th_s_url_6);
    }
    return false;
  }

  private boolean _jspx_meth_c_forEach_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:forEach
    org.apache.taglibs.standard.tag.rt.core.ForEachTag _jspx_th_c_forEach_0 = (org.apache.taglibs.standard.tag.rt.core.ForEachTag) _jspx_tagPool_c_forEach_var_items.get(org.apache.taglibs.standard.tag.rt.core.ForEachTag.class);
    _jspx_th_c_forEach_0.setPageContext(_jspx_page_context);
    _jspx_th_c_forEach_0.setParent(null);
    _jspx_th_c_forEach_0.setItems((java.lang.Object) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${grades }", java.lang.Object.class, (PageContext)_jspx_page_context, null));
    _jspx_th_c_forEach_0.setVar("g");
    int[] _jspx_push_body_count_c_forEach_0 = new int[] { 0 };
    try {
      int _jspx_eval_c_forEach_0 = _jspx_th_c_forEach_0.doStartTag();
      if (_jspx_eval_c_forEach_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        do {
          out.write("\r\n");
          out.write("          \t\t\t<option value=\"");
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${g.gradeId }", java.lang.String.class, (PageContext)_jspx_page_context, null));
          out.write('"');
          out.write('>');
          out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${g.name }", java.lang.String.class, (PageContext)_jspx_page_context, null));
          out.write("</option>\r\n");
          out.write("          \t\t");
          int evalDoAfterBody = _jspx_th_c_forEach_0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
      }
      if (_jspx_th_c_forEach_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_c_forEach_0[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_c_forEach_0.doCatch(_jspx_exception);
    } finally {
      _jspx_th_c_forEach_0.doFinally();
      _jspx_tagPool_c_forEach_var_items.reuse(_jspx_th_c_forEach_0);
    }
    return false;
  }

  private boolean _jspx_meth_s_url_7(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.springframework.web.servlet.tags.UrlTag _jspx_th_s_url_7 = (org.springframework.web.servlet.tags.UrlTag) _jspx_tagPool_s_url_value_nobody.get(org.springframework.web.servlet.tags.UrlTag.class);
    _jspx_th_s_url_7.setPageContext(_jspx_page_context);
    _jspx_th_s_url_7.setParent(null);
    _jspx_th_s_url_7.setValue("/static/easyui/datagrid-cellediting.js");
    int[] _jspx_push_body_count_s_url_7 = new int[] { 0 };
    try {
      int _jspx_eval_s_url_7 = _jspx_th_s_url_7.doStartTag();
      if (_jspx_th_s_url_7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_url_7[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_url_7.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_url_7.doFinally();
      _jspx_tagPool_s_url_value_nobody.reuse(_jspx_th_s_url_7);
    }
    return false;
  }

  private boolean _jspx_meth_s_url_8(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.springframework.web.servlet.tags.UrlTag _jspx_th_s_url_8 = (org.springframework.web.servlet.tags.UrlTag) _jspx_tagPool_s_url_value_nobody.get(org.springframework.web.servlet.tags.UrlTag.class);
    _jspx_th_s_url_8.setPageContext(_jspx_page_context);
    _jspx_th_s_url_8.setParent(null);
    _jspx_th_s_url_8.setValue("/schoolClass/findByGrade");
    int[] _jspx_push_body_count_s_url_8 = new int[] { 0 };
    try {
      int _jspx_eval_s_url_8 = _jspx_th_s_url_8.doStartTag();
      if (_jspx_th_s_url_8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_url_8[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_url_8.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_url_8.doFinally();
      _jspx_tagPool_s_url_value_nobody.reuse(_jspx_th_s_url_8);
    }
    return false;
  }

  private boolean _jspx_meth_s_url_9(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.springframework.web.servlet.tags.UrlTag _jspx_th_s_url_9 = (org.springframework.web.servlet.tags.UrlTag) _jspx_tagPool_s_url_value_nobody.get(org.springframework.web.servlet.tags.UrlTag.class);
    _jspx_th_s_url_9.setPageContext(_jspx_page_context);
    _jspx_th_s_url_9.setParent(null);
    _jspx_th_s_url_9.setValue("/student/findByClass");
    int[] _jspx_push_body_count_s_url_9 = new int[] { 0 };
    try {
      int _jspx_eval_s_url_9 = _jspx_th_s_url_9.doStartTag();
      if (_jspx_th_s_url_9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_url_9[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_url_9.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_url_9.doFinally();
      _jspx_tagPool_s_url_value_nobody.reuse(_jspx_th_s_url_9);
    }
    return false;
  }

  private boolean _jspx_meth_s_url_10(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.springframework.web.servlet.tags.UrlTag _jspx_th_s_url_10 = (org.springframework.web.servlet.tags.UrlTag) _jspx_tagPool_s_url_value_nobody.get(org.springframework.web.servlet.tags.UrlTag.class);
    _jspx_th_s_url_10.setPageContext(_jspx_page_context);
    _jspx_th_s_url_10.setParent(null);
    _jspx_th_s_url_10.setValue("/examResult/upload");
    int[] _jspx_push_body_count_s_url_10 = new int[] { 0 };
    try {
      int _jspx_eval_s_url_10 = _jspx_th_s_url_10.doStartTag();
      if (_jspx_th_s_url_10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_url_10[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_url_10.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_url_10.doFinally();
      _jspx_tagPool_s_url_value_nobody.reuse(_jspx_th_s_url_10);
    }
    return false;
  }

  private boolean _jspx_meth_s_url_11(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.springframework.web.servlet.tags.UrlTag _jspx_th_s_url_11 = (org.springframework.web.servlet.tags.UrlTag) _jspx_tagPool_s_url_value_nobody.get(org.springframework.web.servlet.tags.UrlTag.class);
    _jspx_th_s_url_11.setPageContext(_jspx_page_context);
    _jspx_th_s_url_11.setParent(null);
    _jspx_th_s_url_11.setValue("/grade/edit");
    int[] _jspx_push_body_count_s_url_11 = new int[] { 0 };
    try {
      int _jspx_eval_s_url_11 = _jspx_th_s_url_11.doStartTag();
      if (_jspx_th_s_url_11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_url_11[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_url_11.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_url_11.doFinally();
      _jspx_tagPool_s_url_value_nobody.reuse(_jspx_th_s_url_11);
    }
    return false;
  }

  private boolean _jspx_meth_s_url_12(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.springframework.web.servlet.tags.UrlTag _jspx_th_s_url_12 = (org.springframework.web.servlet.tags.UrlTag) _jspx_tagPool_s_url_value_nobody.get(org.springframework.web.servlet.tags.UrlTag.class);
    _jspx_th_s_url_12.setPageContext(_jspx_page_context);
    _jspx_th_s_url_12.setParent(null);
    _jspx_th_s_url_12.setValue("/lessonSelect/deleteSelect");
    int[] _jspx_push_body_count_s_url_12 = new int[] { 0 };
    try {
      int _jspx_eval_s_url_12 = _jspx_th_s_url_12.doStartTag();
      if (_jspx_th_s_url_12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_url_12[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_url_12.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_url_12.doFinally();
      _jspx_tagPool_s_url_value_nobody.reuse(_jspx_th_s_url_12);
    }
    return false;
  }

  private boolean _jspx_meth_s_url_13(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.springframework.web.servlet.tags.UrlTag _jspx_th_s_url_13 = (org.springframework.web.servlet.tags.UrlTag) _jspx_tagPool_s_url_value_nobody.get(org.springframework.web.servlet.tags.UrlTag.class);
    _jspx_th_s_url_13.setPageContext(_jspx_page_context);
    _jspx_th_s_url_13.setParent(null);
    _jspx_th_s_url_13.setValue("/lessonSelect/lessonForSelect");
    int[] _jspx_push_body_count_s_url_13 = new int[] { 0 };
    try {
      int _jspx_eval_s_url_13 = _jspx_th_s_url_13.doStartTag();
      if (_jspx_th_s_url_13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_url_13[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_url_13.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_url_13.doFinally();
      _jspx_tagPool_s_url_value_nobody.reuse(_jspx_th_s_url_13);
    }
    return false;
  }

  private boolean _jspx_meth_s_url_14(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.springframework.web.servlet.tags.UrlTag _jspx_th_s_url_14 = (org.springframework.web.servlet.tags.UrlTag) _jspx_tagPool_s_url_value_nobody.get(org.springframework.web.servlet.tags.UrlTag.class);
    _jspx_th_s_url_14.setPageContext(_jspx_page_context);
    _jspx_th_s_url_14.setParent(null);
    _jspx_th_s_url_14.setValue("/lessonSelect/selectLesson");
    int[] _jspx_push_body_count_s_url_14 = new int[] { 0 };
    try {
      int _jspx_eval_s_url_14 = _jspx_th_s_url_14.doStartTag();
      if (_jspx_th_s_url_14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_url_14[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_url_14.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_url_14.doFinally();
      _jspx_tagPool_s_url_value_nobody.reuse(_jspx_th_s_url_14);
    }
    return false;
  }

  private boolean _jspx_meth_s_url_15(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.springframework.web.servlet.tags.UrlTag _jspx_th_s_url_15 = (org.springframework.web.servlet.tags.UrlTag) _jspx_tagPool_s_url_value_nobody.get(org.springframework.web.servlet.tags.UrlTag.class);
    _jspx_th_s_url_15.setPageContext(_jspx_page_context);
    _jspx_th_s_url_15.setParent(null);
    _jspx_th_s_url_15.setValue("/grade/get");
    int[] _jspx_push_body_count_s_url_15 = new int[] { 0 };
    try {
      int _jspx_eval_s_url_15 = _jspx_th_s_url_15.doStartTag();
      if (_jspx_th_s_url_15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_url_15[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_url_15.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_url_15.doFinally();
      _jspx_tagPool_s_url_value_nobody.reuse(_jspx_th_s_url_15);
    }
    return false;
  }

  private boolean _jspx_meth_s_url_16(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.springframework.web.servlet.tags.UrlTag _jspx_th_s_url_16 = (org.springframework.web.servlet.tags.UrlTag) _jspx_tagPool_s_url_value_nobody.get(org.springframework.web.servlet.tags.UrlTag.class);
    _jspx_th_s_url_16.setPageContext(_jspx_page_context);
    _jspx_th_s_url_16.setParent(null);
    _jspx_th_s_url_16.setValue("/lessonSelect/lessonSelected");
    int[] _jspx_push_body_count_s_url_16 = new int[] { 0 };
    try {
      int _jspx_eval_s_url_16 = _jspx_th_s_url_16.doStartTag();
      if (_jspx_th_s_url_16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_url_16[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_url_16.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_url_16.doFinally();
      _jspx_tagPool_s_url_value_nobody.reuse(_jspx_th_s_url_16);
    }
    return false;
  }

  private boolean _jspx_meth_s_url_17(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.springframework.web.servlet.tags.UrlTag _jspx_th_s_url_17 = (org.springframework.web.servlet.tags.UrlTag) _jspx_tagPool_s_url_value_nobody.get(org.springframework.web.servlet.tags.UrlTag.class);
    _jspx_th_s_url_17.setPageContext(_jspx_page_context);
    _jspx_th_s_url_17.setParent(null);
    _jspx_th_s_url_17.setValue("/examResult/save");
    int[] _jspx_push_body_count_s_url_17 = new int[] { 0 };
    try {
      int _jspx_eval_s_url_17 = _jspx_th_s_url_17.doStartTag();
      if (_jspx_th_s_url_17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        return true;
      }
    } catch (Throwable _jspx_exception) {
      while (_jspx_push_body_count_s_url_17[0]-- > 0)
        out = _jspx_page_context.popBody();
      _jspx_th_s_url_17.doCatch(_jspx_exception);
    } finally {
      _jspx_th_s_url_17.doFinally();
      _jspx_tagPool_s_url_value_nobody.reuse(_jspx_th_s_url_17);
    }
    return false;
  }
}
