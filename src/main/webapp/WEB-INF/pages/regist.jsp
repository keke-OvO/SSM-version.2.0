<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>注册界面</title>
    <link rel="stylesheet" href="/layui/css/layui.css">
    <link rel="stylesheet" href="/layui/css/style.css">
    <script src="/layui/layui.js" ></script>
</head>

<body>
<div class="login-main">
    <header class="layui-elip" style="width: 82%;;margin-top:40px">注册页</header>
    <!-- 表单选项 -->
    <form class="layui-form" action="" method="post">
        <!--学号-->
        <div class="layui-input-inline">
            <div class="layui-inline" style="width: 85%">
                <input type="text" id="user" name="sno" required
                       lay-verify="required" placeholder="请输入学号" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <!-- 密码 -->
        <div class="layui-input-inline">
            <div class="layui-inline" style="width: 85%">
                <input type="password" id="pwd" name="pwd" required
                       lay-verify="required" placeholder="请输入密码"
                       autocomplete="off" class="layui-input">
            </div>
        </div>
        <!-- 确认密码 -->
        <div class="layui-input-inline">
            <div class="layui-inline" style="width: 85%">
                <input type="password" id="rpwd"  required
                       lay-verify="required" placeholder="请确认密码"
                       autocomplete="off" class="layui-input">
            </div>
            <!-- 对号 -->
            <div class="layui-inline">
                <i class="layui-icon" id="rpri" style="color: green;font-weight: bolder;" hidden></i>
            </div>
            <!-- 错号 -->
            <div class="layui-inline">
                <i class="layui-icon" id="rpwr" style="color: red; font-weight: bolder;" hidden>ဆ</i>
            </div>
        </div>


        <div class="layui-input-inline">
            <div class="layui-inline" style="width: 85%">
                <input type="text" name="name" required
                       lay-verify="required" placeholder="姓名" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-input-inline">
            <div class="layui-inline" style="width: 85%">
                <input type="text" name="major" required
                       lay-verify="required" placeholder="xx级xx专业" autocomplete="off"
                       class="layui-input">
            </div>
        </div>
        <div class="layui-input-inline">
            <div class="layui-inline" style="width: 85%">
                <div class="layui-input-inline">
                    <input type="text" name="birth"
                           lay-verify="required" placeholder="出生日期" autocomplete="off"
                           class="layui-input" id="test1">
                </div>
            </div>
        </div>
        <div class="layui-input-inline login-btn" style="width: 85%">
            <button type="submit" lay-submit
                    lay-filter="regist" class="layui-btn">注册</button>
        </div>
        <hr style="width: 85%" />
        <p style="width: 85%"><a href="/book/tologin" class="fl">已有账号？立即登录</a></p>
    </form>
</div>

<script>
    layui.use(['form','jquery','laydate','layer'], function () {
        var form   = layui.form;
        var $      = layui.jquery;
        var layer  = layui.layer;
        var laydate = layui.laydate;

        //执行一个laydate实例
        laydate.render({
            elem: '#test1' //指定元素
            ,trigger:'click'
        });

        layer.tips('学号就是借阅号哦!', '#user');

        //验证两次密码是否一致
        $('#rpwd').blur(function() {
            if($('#pwd').val() != $('#rpwd').val()){
                $('#rpwr').removeAttr('hidden');
                $('#rpri').attr('hidden','hidden');
                layer.msg('两次输入密码不一致!');
            }else {
                $('#rpri').removeAttr('hidden');
                $('#rpwr').attr('hidden','hidden');
            };
        });


        //添加表单监听事件,提交注册信息
        form.on('submit(regist)', function(data) {
            if(!new RegExp("^[0-9]*$").test(data.field.sno)){
                layer.msg("学号为数字哦!");
                return false;
            }
            $.ajax({
                url:'/book/regist',
                data:data.field,
                dataType:'json',
                type:'post',
                success:function(data){
                        layer.msg(data.msg);
                        location.href = "/book/tologin";
                }
            })
            //防止页面跳转
            return false;
        });

    });
</script>

</body>
</html>
