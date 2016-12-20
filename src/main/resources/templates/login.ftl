<#include "/header.ftl">
<div class="container">
    <form class="form-signin" action="login" method="post">
        <h2 class="form-signin-heading">请登录</h2>
        <label for="inputEmail" class="sr-only">用户名</label>
        <input type="text" name="username" class="form-control" placeholder="用户名" autofocus="">
        <label for="inputPassword" class="sr-only">密码</label>
        <input type="password" name="password" class="form-control" placeholder="密码">
        <#--<div class="checkbox">-->
            <#--<label>-->
                <#--<input type="checkbox" value="remember-me"> Remember me-->
            <#--</label>-->
        <#--</div>-->
        <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
    </form>

</div>
<#--<form action="login" method="post">-->
    <#--用户名:<input type="text" name="username">-->
    <#--密码:<input type="password" name="password">-->
    <#--<input type="submit" value="登录">-->
<#--</form>-->
<#include "/footer.ftl">