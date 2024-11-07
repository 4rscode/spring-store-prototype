<#import "blocks/template.ftlh" as t>

<@t.template user>
    <h1 style="text-align: center">Поиск услуги</h1>
    <hr>
    <form method="get" action="/products">
        <div class="form-row">
            <div class="form-group col-md-2">
                <label for="searchCity">Город</label>
                <select id="searchCity" name="searchCity" class="form-control">
                    <option value="">Город не выбран</option>
                    <option value="Москва">Москва</option>
                    <option value="Санкт-Петербург">Санкт-Петербург</option>
                    <option value="Новосибирск">Новосибирск</option>
                    <option value="Красноярск">Красноярск</option>
                    <option value="Екатеринбург">Екатеринбург</option>
                    <option value="Самара">Самара</option>
                    <option value="Орск">Орск</option>
                    <option value="Казань">Казань</option>
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
</@t.template>
