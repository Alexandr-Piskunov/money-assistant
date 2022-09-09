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
                        <a class="nav-link" href="/category">
                            <span class="align-text-bottom"></span>
                            Вернуться
                        </a>
                </ul>
            </div>
        </nav>

        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h1 class="h2">Просмотр и создание отчета доходов по категории</h1>
            </div>
            <form action="/report-category/create-plus/" method="GET">
                <div class="row g-3 align-items-center">
                    <div class="col-auto">
                        <label for="inputDateStart" class="form-label"></label>
                        <input class="form-control" type="date" id="start" name="start">
                    </div>
                    <span id="HelpInline" class="form-text">
                        Дата начала отчета
                    </span>
                </div>
                <p></p>
                    <div class="row g-3 align-items-center">
                        <div class="col-auto">
                            <label for="inputEndDate" class="form-label"></label>
                            <input class="form-control" type="date" name="end" id="end">
                        </div>
                        <span id="HelpInline" class="form-text">
                 Дата окончания отчета
                      </span>
                        <p></p>
                    </div>
                    <button type="submit" class="btn btn-sm btn-outline-success" name="id" value="${id}">Создать
                    </button>
            </form>

            <p></p>
            <div class="table-responsive">
                <table class="table table-striped table-sm">
                    <thead>
                    <tr>
                        <th scope="col">Название категории</th>
                        <th scope="col">Сумма</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <th scope="row">${name}</th>
                        <td>${amount} </td>
                    </tr>
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
