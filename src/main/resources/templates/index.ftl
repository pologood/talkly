<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1>Hello World</h1>
<ul>
<#list users as u>
    <a href="/guest?to=${u}">${u}</a>
</#list>
</ul>
</body>
</html>