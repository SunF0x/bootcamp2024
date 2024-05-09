# bootcamp2024
В папке test лежат тесты к API сервиса Ромашки. 

Результат выполнения тестов представлен ниже:

<img src="https://github.com/SunF0x/bootcamp2024/assets/57665335/a4d83809-3582-470e-902f-74f599205bd9" width="400" />

В папке test файл [Tests_balance](src/test/Tests_balance.java) отвечает за проверку вычисления баланса пользователей и проверку списания.
Для проверки был сгенерирован CDR

<img src="https://github.com/SunF0x/bootcamp2024/assets/57665335/fa3d4e51-6d17-4b43-8bb9-35acfc031916" width="400" />
<img src="https://github.com/SunF0x/bootcamp2024/assets/57665335/faededf6-84a9-4c62-913a-22a68dbd066b" width="400" />

Был сделан скрин базы данных до и после генерации и старта списания:

<img src="https://github.com/SunF0x/bootcamp2024/assets/57665335/dbc4a4f9-60c5-4162-8526-5f9da7965545" width="400" />
<img src="https://github.com/SunF0x/bootcamp2024/assets/57665335/a143e7c7-8f31-4a91-aa4a-b7ede3a6d1d6" width="400" />

После этого был выполнен запрос API на получение баланса пользователя из поднятного BRT сервиса (статус 200, баланс выводится в консоль).

<img src="https://github.com/SunF0x/bootcamp2024/assets/57665335/909a5118-0b39-4d36-aff8-0d85c5d8079a" width="400" />

После этого были написаны тесты для проверки вычисления правильного баланса:
1) Тариф 11 - списание за один месяц
2) Тариф 11 - списание за несколько месяцев
3) Тариф 12 - списание за один месяц
4) Тариф 12 - списание за несколько месяцев

В результате все тесты были пройдены.
Для взаимодействия с тестами надо выбрать пользователя, записать его id, найти в CDR данные о его звонках и внести время в тесты.

