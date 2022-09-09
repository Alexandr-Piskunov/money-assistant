<#import "/spring.ftl" as spring />

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Регистрация</title>

    <link href="/css/bootstrap.min.css" rel="stylesheet">

    <link href="/css/signin.css" rel="stylesheet">
</head>
<body class="text-center">

<main class="form-signin w-100 m-auto">
    <form action="/registration" method="post">
        <h1 class="h3 mb-3 fw-normal">Введите необходимые данные</h1>
        <div class="form-floating">
            <@spring.formInput "form.email" "class=\"form-control\" id=\"floatingInput\" placeholder=\"Enter email\"" "email"/>
            <label for="floatingInput">Email</label>
            <@spring.showErrors "<br>"/>
        </div>
        <div class="form-floating">
            <@spring.formInput "form.name" "class=\"form-control\" id=\"floatingName\" placeholder=\"Name\"" "name"/>
            <label for="floatingName">Имя пользователя</label>
            <@spring.showErrors "<br>"/>
        </div>
        <div class="form-floating">
            <@spring.formInput "form.password" "class=\"form-control\" id=\"floatingPassword\" placeholder=\"Password\"" "password"/>
            <label for="floatingPassword">Пароль</label>
            <@spring.showErrors "<br>"/>
        </div>
        <button class="w-100 btn btn-lg btn-outline-success" type="submit">Зарегистрироваться</button>
        <p></p>
    </form>
</main>
</body>
</html>