<!doctype html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
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
                <h1 class="h2">Создание перевода</h1>
            </div>
            <#if error??>
                <div class="alert alert-danger" role="alert">
                    Произошла ошибка при создании транзакции, возможно неверно выбраны счета или сумма транзакции превышает баланс счета списания
                </div>
            </#if>
            <#if badAmount??>
                <div class="alert alert-danger" role="alert">
                    Произошла ошибка при создании транзакции, неверная сумма для перевода
                </div>
            </#if>
            <#if check??>
                <div class="alert alert-primary" role="alert">
                    Поля 'Категории транзакции' и 'Сумма перевода' обязательные для заполнения,
                    если необходимо перевести/отправить деньги на счет который не зарегистрирован в системе - просто оставьте это поле пустым
                </div>
            </#if>
            <form action="/transaction/create" method="get">
                Счет с которого спишутся деньги
                <select class="form-select" name="fromAccountId">
                    <option selected value="">Выберите счет</option>
                    <#list accounts as a>
                        <option value="${a.getId()}">Имя: ${a.getAccountName()}, Баланс: ${a.getBalance()}</option>
                    </#list>
                </select>
                <p></p>
                Счет на который нужно зачислить деньги
                <select class="form-select"  name="toAccountId">
                    <option selected value="">Выберите счет</option>
                    <#list accounts as a>
                     <option value="${a.getId()}">Имя: ${a.getAccountName()}, Баланс: ${a.getBalance()}</option>
                    </#list>
                </select>
                <p></p>
                <div class="mb-3">
                    <label for="InputAmount" class="form-label">Сумма перевода</label>
                    <input class="form-control" name="amount" id="amount">
                </div>
                <p></p>
                Выберите одну, или несколько категорий для этой транзакции
                <select class="form-select" size="3" multiple="multiple" name="categoryId">
                    <#list categories as ct>
                        <option value="${ct.getId()}">${ct.getCategoryName()}</option>
                    </#list>
                </select>
                <button type="submit" class="btn btn-sm btn-outline-success">Создать</button>
            </form>
        </main>
    </div>
</div>
<script src="/js/bootstrap.bundle.min.js"></script>
<script src="/js/dashboard.js"></script>
</body>
</html>