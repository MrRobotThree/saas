<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ include file="../../base.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <!-- 页面meta -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>数据 - AdminLTE2定制版</title>
    <meta name="description" content="AdminLTE2定制版">
    <meta name="keywords" content="AdminLTE2定制版">
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" name="viewport">
    <!-- 页面meta /-->
</head>
<body>
<div id="frameContent" class="content-wrapper" style="margin-left:0px;">
    <!-- 内容头部 -->
    <section class="content-header">
        <h1>
            货运管理
            <small>添加装箱单</small>
        </h1>
        <ol class="breadcrumb">
            <li><a href="all-admin-index.html"><i class="fa fa-dashboard"></i> 首页</a></li>
            <li><a href="all-order-manage-list.html">货运管理</a></li>
            <li class="active">装箱单添加及报运单列表</li>
        </ol>
    </section>
    <!-- 内容头部 /-->

    <!-- 正文区域 -->
    <section class="content">

        <!--订单信息-->
        <div class="panel panel-default">
            <div class="panel-heading">新增装箱单</div>
            <script>

            </script>
            <%--enctype="multipart/form-data"--%>
            <form id="editForm" action="${ctx}/cargo/packing/edit.do" method="post" enctype="multipart/form-data">
                <%--<input type="hidden" name="packingListId" value="${packingListId}">--%>
                <div class="row data-type" style="margin: 0px">
                    <div class="col-md-2 title">卖方</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="卖方" name="seller"
                               value="${packing.seller}">
                    </div>

                    <div class="col-md-2 title">买方</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="买方" name="buyer" value="${packing.buyer}">
                    </div>

                    <div class="col-md-2 title">发票号</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="发票号" name="invoiceNo"
                               value="${packing.invoiceNo}">
                    </div>

                    <div class="col-md-2 title">发票日期</div>
                    <div class="col-md-4 data">
                        <div class="input-group date">
                            <div class="input-group-addon">
                                <i class="fa fa-calendar"></i>
                            </div>
                            <input type="text" placeholder="发票日期" name="invoiceDate" class="form-control pull-right"
                                   value="<fmt:formatDate value="${packing.invoiceDate}" pattern="yyyy-MM-dd"/>"
                                   id="deliveryPeriod">
                        </div>
                    </div>

                    <div class="col-md-2 title">唛头</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="唛头" name="marks" value="${packing.marks}">
                    </div>

                    <div class="col-md-2 title">描述</div>
                    <div class="col-md-4 data">
                        <input type="text" class="form-control" placeholder="描述" name="descriptions"
                               value="${packing.descriptions}">
                    </div>

                </div>

                <!--订单信息/-->

                <!--工具栏-->
                <div class="box-tools text-center">
                    <button type="button" onclick='document.getElementById("editForm").submit()' class="btn bg-maroon">
                        保存
                    </button>
                    <button type="button" class="btn bg-default" onclick="history.back(-1);">返回</button>
                </div>
                <!--工具栏/-->

                <!-- 正文区域 /-->

                <section class="content">

                    <!-- .box-body -->
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title">报运单列表</h3>
                        </div>

                        <div class="box-body">

                            <!-- 数据表格 -->
                            <div class="table-box">
                                <!--数据列表-->
                                <table id="dataList" class="table table-bordered table-striped table-hover dataTable">
                                    <thead>
                                    <tr>
                                        <td><input type="checkbox" name="selid" onclick="checkAll('id',this)"></td>
                                        <th class="sorting">报运号</th>
                                        <th class="sorting">货物/附件</th>
                                        <th class="sorting">信用证号</th>
                                        <th class="sorting">收货地址</th>
                                        <th class="sorting">装运港</th>
                                        <th class="sorting">目的港</th>
                                        <th class="sorting">运输方式</th>
                                        <th class="sorting">价格条件</th>
                                        <th class="sorting">状态</th>
                                        <%--   <th class="text-center">操作</th>--%>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${page.list}" var="o" varStatus="status">
                                        <tr class="odd" onmouseover="this.className='highlight'"
                                            onmouseout="this.className='odd'">
                                            <td><input type="checkbox" name="id" value="${o.id}" id="exportId"/></td>
                                            <td>${o.id}</td>
                                            <td align="center">
                                                    ${o.proNum}/${o.extNum}
                                            </td>
                                            <td>${o.lcno}</td>
                                            <td>${o.consignee}</td>
                                            <td>${o.shipmentPort}</td>
                                            <td>${o.destinationPort}</td>
                                            <td>${o.transportMode}</td>
                                            <td>${o.priceCondition}</td>
                                            <td>
                                                <c:if test="${o.state==0}">草稿</c:if>
                                                <c:if test="${o.state==1}"><font color="green">已上报</font></c:if>
                                                <c:if test="${o.state==2}"><font color="red">已装箱</font></c:if>
                                            </td>
                                                <%-- <td>
                                                     <a href="${ctx }/cargo/packing/edit.do?id=${o.id}">[装箱]</a>
                                                 </td>--%>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                                <!--工具栏/-->
                            </div>
                            <!-- 数据表格 /-->
                        </div>
                        <!-- /.box-body -->

                        <!-- .box-footer-->
                        <div class="box-footer">
                            <jsp:include page="../../common/page.jsp">
                                <jsp:param value="/cargo/packing/list.do?packingListId=${packingListId}"
                                           name="pageUrl"/>
                            </jsp:include>
                            <!-- /.box-footer-->
            </form>
        </div>
    </section>

</div>
<!-- 内容区域 /-->
</body>
<script>
    $('#deliveryPeriod').datepicker({
        autoclose: true,
        format: 'yyyy-mm-dd'
    });

</script>
</html>