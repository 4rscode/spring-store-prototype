<#import "blocks/template.ftlh" as t>

<@t.template user!>
    <h1 style="text-align: center">Поиск услуги</h1>
    <hr>
    <form method="get" action="/products">
        <div class="form-row">
            <div class="form-group col-md-2">
                <label for="searchCity">Город</label>
                <select id="searchCity" name="searchCity" class="form-control">
                    <option value="">Город не выбран</option>
                    <option value="Москва" <#if searchCity == "Москва">selected</#if>>Москва</option>
                    <option value="Санкт-Петербург" <#if searchCity == "Санкт-Петербург">selected</#if>>Санкт-Петербург</option>
                    <option value="Новосибирск" <#if searchCity == "Новосибирск">selected</#if>>Новосибирск</option>
                    <option value="Красноярск" <#if searchCity == "Красноярск">selected</#if>>Красноярск</option>
                    <option value="Екатеринбург" <#if searchCity == "Екатеринбург">selected</#if>>Екатеринбург</option>
                    <option value="Самара" <#if searchCity == "Самара">selected</#if>>Самара</option>
                    <option value="Орск" <#if searchCity == "Орск">selected</#if>>Орск</option>
                    <option value="Казань" <#if searchCity == "Казань">selected</#if>>Казань</option>
                </select>
            </div>
            <div class="form-group col-md-10">
                <label for="searchWord">Поиск по объявлениям</label>
                <input type="text" name="searchWord" <#if searchWord??>value="${searchWord}"</#if> class="form-control"
                       id="searchWord">
            </div>
            <button style="width: 100%" type="submit" class="btn btn-dark">Поиск</button>
        </div>
    </form>
    <hr>
    <div class="card-columns">
        <#list products as product>
            <a href="/product/${product.id}" class="product-link">
                <div class="card text-white bg-dark my-3 product">
                    <img src="/images/${product.previewImageId}" class="card-img-top">
                    <div class="card-body">
                        <h5 class="card-title">${product.title}</h5>
                        <p class="card-text">${product.price} ₽</p>
                    </div>
                    <div class="card-footer">
                        ${product.city}
                    </div>
                </div>
            </a>
        </#list>
        <#if products?size == 0>
            <h1 style="text-align: center">Не найдено</h1>
        </#if>
    </div>
</@t.template>
