<#include "/header.ftl">
<h1>Hello World</h1>
<ul>
<#list users as u>
    <a href="/guest?to=${u}">${u}</a>
</#list>
</ul>
<#include "/footer.ftl">