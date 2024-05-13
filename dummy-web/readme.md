# 2. Zárthelyi Dolgozat - RESTful Web API

Amennyiben a gyakorlás a cél, úgy töröld a [webapi](webapi) mappát, és hajrá!
Akkor érezhetjük jól magunkat, ha az összes teszt hiba nélkül fut!
Ezeket futtathatod lokálisan.

Az implementálandó rész hozzávetőlegesen effektíve 110 sorból áll,
ennél jobban ne igazán bonyolítsuk túl.

Ujjgyakorlat szintideje: ~60 perc

Ha ezt teljesíteni tudjuk, biztosan nem kéne legyen semmi gondunk később.

```bash
./mvnw clean test
```

## Feladatok

### Read - GET

Készítsünk egy `ReadServlet`-et `/api/subject/read`, és `/api/subject/list` endpoint specifikációkkal.
- Siker esetén a visszaadott státusz kód legyen `OK` (200).
- A válasz típusa egy json kompatibilis string legyen (most egy lista).
- A lista az adatbázisban található osztályokat kell hogy tartalmazza!
- A lista elemeiben legyen megtalálható az összes mező ugyanolyan néven, mint a tárolt modell osztályban is.
- A servletnek feladata szűrést is végezni a lehetséges modell paraméterek szerint
  (a `null` értékű szűrési feltételekkel nem foglalkozunk).
  Ezt a szűrést URL paraméterek segítségével valósítsuk meg!
  Az egyes paraméterek neve pontosan megegyezik a modellben találhahtó mezők nevével.
- A válasz `BAD_REQUEST` (400), amennyiben
    - bármelyik szűrő feltétel nem olvasható be az annak megfelelő típusba, valamint
    - ha a `someBool` filter **true**, vagy **false** értéken kívül bármi mást vesz fel (kis és nagy betű nem számít).
      Azaz, ha a kérésünkben pl. `someBool=1` GET paraméter található, az egy rossz formátum.
- A servlet ezen kívül kezeljen még két sütit:
  - `full-size` cookie, melynek értéke az adatbázisban található összes modell számossága,
  - `current-size` cookie, melynek értéke az éppen aktuális lekérdezésben található elemek számossága. 

### Create - POST

Készítsünk egy `CreateServlet`-et `/api/dummy/create`, és `/api/dummy/insert` endpoint specifikációkkal.
- Siker esetén a visszaadott státusz kód legyen `CREATED` (201).
- A kérést egy JSON string formájában kell feldolgozni!
  - A mezők között az id-t nem szükséges szerepeltetni,
  - azonban minden más mező szerepeljen ugyanazon névvel,
    mint ahogyan az a modell osztályban is megtalálható.
- A válasz típusa egy JSON kompatibilis string legyen.
- A válaszban legyen található egy `id` mező, amely alatt az éppen elmentett rekord elsődleges kulcsa található.
- A servletnek feladata elmentenie a küldött modellt is, amennyiben ez végrehajtható.
- A válasz `BAD_REQUEST` (400), amennyiben
  - a kérés üres (request body üres)
  - ebben az esetben nem szabad, hogy mentés történjen

### Delete - DELETE

Készítsünk egy `DeleteServlet`-et `/api/dummy/delete`, és `/api/dummy/remove` endpoint specifikációkkal.
- Siker esetén a válasz státusza `NO_CONTENT` (204).
- Az id-t egy `id` nevű URL paraméter alatt keressük.
- A kérés elvégzésekor az adott modellt ki kell törölni az adatbázisból!
- Nem létező id törlésekor nincs hiba, de nem is történik semmi.
- Amennyiben a küldött kérés üres, avagy az id mező nem értelmezhető, úgy a válasz `BAD_REQUEST` (400).

### Update - PUT

Készítsünk egy `UpdateServlet`-et `/api/dummy/update`, és `/api/dummy/refresh` endpoint specifikációkkal.
- Az alapértelmezett válasz státusz kódja legyen `NO_CONTENT` (204).
- A frissíteni kívánt modellre vonatkozó kérést egy JSON string formátumban várjuk,
  melynek struktúrája megegyezik a frissíteni kívánt modellével. Azaz van neki elsődleges kulcsa, `id` mezeje,
  valamint a frissíteni kívánt adatokra vonatkozó mezőkkel is rendelkezik,
  melyek neve egyező a modellben található mezők nevével.
- Amennyiben a frissíteni kívánt `id` létezik, úgy frissítsük a rekordot a db-ben is.
- Ha nem létezik az id, azonban valid, akkor a válaszunk státusza `CREATED` (201) legyen,
  és hozzuk is létre ezt a rekordot evvel az `id`-vel. (ezt csinálja egy PUT)
- Térjünk vissza `BAD_REQUEST` (400) státusszal, amennyiben az alábbiak bármelyike fennáll:
  - A kérés üres, vagy
  - a megadott `id` üres.
- Hiba esetén ne frissüljön az adatbázis!

## Core - make your life easier

Ezek itt nem feladatok, ez csak segédlet.

A dao használata egyértelmű a nevezéktan,
és [Dao](core/src/main/java/hu/szte/inf/core/data/Dao.java) dokumentáció alapján.

Egy erre vonatkozó implementációt a következőképpen kérhetünk a legegyszerűbben:

[Instancer](core/src/main/java/hu/szte/inf/core/util/common/Instancer.java)
```java
Dao<?, ?> dao = Instancer.defaultDaoWithSimpleDs();
...
Dao<?, ?> dao = Instancer.defaultDaoWithHikariDs();
```

A különbség a kettő között pusztán a `DataSource` implementációban van, használatuk ekvivalens.
A konfigurációt mindketten az `application.properties` fájlból szedik.
Ezt érdemes a webapi resource alatt szerkeszteni.

Természetesen a `?` helyére a megfelelő típusokat kellene elhelyezni,
más különben gondok *lehetnek*.

[Converter](core/src/main/java/hu/szte/inf/core/util/common/Converter.java)

```java
List<?> mylist = Converter.iterableToList(dao.findAll());
```

Amennyiben nem szeretjük az iterálható interface-t használni, avagy erős ellenérzéseket vált ki belőlünk,
úgy konvertáljuk őket listává a Converter class segítségével.

### Db rekreáció
[Main](core/src/main/java/hu/szte/inf/core/Main.java) fájlt futtatva kaphatunk egy friss main.db fájlt.
Ha nem akarjuk paraméterezve futtatni/bővíteni, akkor töröljük ki az argumentum beolvasásokat.

## cURL példák

Ezt a részt, amennyiben kicsit hosszúnak találjuk a readme-t, kimásolhatjuk egy külön .md fájlba,
hogy onnen futtassuk a teszt parancsokat.
A lenti parancsok egyszerűen kiadhatók terminálból is, de itt meg is maradnak.

- -X, --request: http request method (GET, POST, PUT, DELETE, stb...)
- -i, --include: a válaszban legyen ott a header információ
- -d, --data: küldeni kívánt adatok ide jöhetnek
- -H, --header: header információ

### Read

```bash
curl -i "http://localhost:8080/api/dummy/read"
```

```bash
curl -i "http://localhost:8080/api/dummy/read?someString=a&someBool=true"
```

Vészesetben megadhatjuk a `Content-Type`-ot is.

```bash
curl -i "http://localhost:8080/api/dummy/read?someString=a&someBool=true" -H 'Content-Type: application/json'
```

### Create

```bash
curl -i "http://localhost:8080/api/dummy/create" -d '{"someString":"My new dummy","anotherString":"NEWCODE-G3","someBool":true,"someInt":5,"otherInt":4}'
```

### Delete

```bash
curl -iX DELETE "http://localhost:8080/api/dummy/delete?id=9"
```

### Update

```bash
curl -iX PUT "http://localhost:8080/api/dummy/update" -d '{"id":9,"anotherString":"NEWER-G3"}' -H 'Content-Type: application/json'
```

```bash
curl -iX PUT "http://localhost:8080/api/dummy/update" -d '{"id":555,"someString":"Some dummy","anotherString":"wololoo","someBool":true,"someInt":3,"otherInt":5}' -H 'Content-Type: application/json'
```

# THE END
