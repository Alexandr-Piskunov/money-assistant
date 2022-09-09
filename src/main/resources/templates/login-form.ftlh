<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Меню авторизации</title>

    <link href="/css/bootstrap.min.css" rel="stylesheet">

    <link href="/css/signin.css" rel="stylesheet">
</head>
<body class="text-center">

<main class="form-signin w-100 m-auto">
    <form action="/login" method="post">
        <h1 class="h3 mb-3 fw-normal">Пожалуйста авторизуйтесь</h1>
        <div class="form-floating">
            <input type="email" name="email"  class="form-control" id="floatingInput" placeholder="name@example.com">
            <label for="floatingInput">Email</label>
        </div>
        <div class="form-floating">
            <input type="password" name="password" class="form-control" id="floatingPassword" placeholder="Password">
            <label for="floatingPassword">Пароль</label>
        </div>
        <button class="w-100 btn btn-lg btn-outline-primary" type="submit">Войти</button>
        <p></p>
    </form>
    <a class="btn btn-lg btn-outline-success" href="/registration" role="button">Зарегистрироваться</a>
</main>
</body>
</html>
