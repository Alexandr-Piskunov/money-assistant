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
        <div class="nav-item text-nowrap">
            <a class="nav-link px-3" href="/logout">Sign out</a>
        </div>
    </div>
</header>
<div class="container-fluid">
    <div class="row">
        <nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block bg-light sidebar collapse">
            <div class="position-sticky pt-3">
                <ul class="nav flex-column">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="/account">
                            <span class="align-text-bottom"></span>
                            Список счетов
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/category">
                            <span class="align-text-bottom"></span>
                            Категории
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="transaction/create">
                            <span class="align-text-bottom"></span>
                            Сделать перевод
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/personal-area">
                            <span class="align-text-bottom"></span>
                            Профиль
                        </a>
                    </li>
                </ul>
            </div>
        </nav>

        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h1 class="h2">Список счетов</h1>
                <div class="btn-toolbar mb-2 mb-md-0">
                    <a class="btn btn-sm btn-outline-success" href="account/create" role="button">Добавить новый счет</a>
                </div>
            </div>

            <div class="table-responsive">
                <table class="table table-striped table-sm">
                    <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">Название счета</th>
                        <th scope="col">Баланс</th>
                        <th scope="col">Действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list account as a>
                        <tr>
                            <th scope="row">${a.getId()}</th>
                            <td>${a.getAccountName()} </td>
                            <td>${a.getBalance()}</td>
                            <td>
                                <div class="btn-toolbar mb-2 mb-md-0">
                                    <form action="account/delete/${a.getId()}" method="post">
                                        <button type="submit" class="btn btn-sm btn-outline-danger">Удалить</button>
                                    </form>
                                    <a class="btn btn-sm btn-outline-secondary" href="account/rename/${a.getId()}"
                                       role="button">Переименовать</a>
                                </div>
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </main>
    </div>
</div>


<script src="/js/bootstrap.bundle.min.js"></script>


<script src="/js/dashboard.js"></script>
</body>
</html>