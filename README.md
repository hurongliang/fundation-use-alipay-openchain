source code for blog post `http://hurongliang.com/2020/08/18/fundation-use-alipay-openchain.html`

useage:

config [mychain] section in file `src/main/resources/application.yml`

```bash
mvn clean package
```

```bash
java -jar target/blockchain-service-1.0-SNAPSHOT.jar
```

http post `http://localhost:10080/blockchain/deposit` with form:

`content` = `this is a test reposit data`

response text is an hash string

http get `http://localhost:10080/blockchain/query-transaction?hash=<hash string responsed after calling url /blochchain/deposit>`

response text is :

```txt
this is a test reposit data
```
