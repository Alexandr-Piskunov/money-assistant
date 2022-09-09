<#import "/spring.ftl" as spring />

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Account</title>
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/dashboard.css" rel="stylesheet">
</head>
<body>
<header class="navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0 shadow">
    <a class="navbar-brand col-md-3 col-lg-2 me-0 px-3 fs-6">Jborn WEB Piskunov</a>
    <div class="navbar-nav">
    </div>
</header>
<div class="container-fluid">
    <div class="row">
        <nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block bg-light sidebar collapse">
            <div class="position-sticky pt-3">
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link" href="/account">
                            <span class="align-text-bottom"></span>
                            Вернуться
                        </a>
                </ul>
            </div>
        </nav>

        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h1 class="h2">Придумайте имя нового счета</h1>
            </div>

            <form action="/account/create" method="post">
                <div class="mb-3">
                    <label for="exampleInputAccountName" class="form-label">Имя счёта</label>
                    <@spring.formInput "form.name" "class=\"form-control\" id=\"exampleInputAccountName\" placeholder=\"Введите имя счета\"" "name"/>
                    <@spring.showErrors "<br>"/>
                </div>
                <button type="submit" class="btn btn-sm btn-outline-success">Создать</button>
            </form>
        </main>
    </div>
</div>
<script src="/js/bootstrap.bundle.min.js"></script>


<script src="/js/dashboard.js"></script>
</body>
</html>