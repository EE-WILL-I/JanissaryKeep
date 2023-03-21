# JanissaryKeep
Project created special for my group mate Ilia to help him with his graduate work


-------------------------------
---СПЕЦИФИКАЦИЯ ПРОЕКТА v1.0---
-------------------------------

### **Название продукта**: "JanissaryKeep" (от слова янычар, ибо куда без османов)

**Краткое описание**: Разработка представляет собой десктоп приложение на Java 18 + JavaFX + MongoDB

**Назначение**: Защита данных архивных электронных документов в образовательной организации высшего образования

**Цель**: Архивировать и защищать данные (файлы) в базе данных

  **Функции**:

    - работа с документами (Word, Excel, PDF, PNG), загружать файлы в\из приложения,
    - зашифровывать и расшифровывать данные,
    - управление ключами, подписями и осуществление аутентификации (проверка подлинности) сообщений,
    - вычислять криптографические хэши,
    - устанавливать соединение с БД,
    - загружать зашифрованные данные в БД,
    - предоставлять пользователю защищенный API и хранить данные о пользователе,
    - скачивать файлы с базы данных,
    - иметь функциональный UI

**UX (User experience) & UI (User interface) Design**:
    
    Use case #0: пользователь запускает программу
    - при запуске приложения пользователь видит окно "Авторизация". в нем необходимо ввести учетные данные или создать профиль (UC #9)
    - если авторизация провалилась, пользователю предлагается повторить попытку
    - после авторизации загружаются данные пользователя из базы данных (id, логин, созданные документы). далее открывается основное окно программы

    Use case #1: пользователь просматривает список загруженных им файлом и связанные с ними данные
    - пользователь переходит во вкладку "Хранилище". в ней находится панель "Документы" со списком всех доступных пользователю документов
    - в панеле "Документы" можно задать фильтры для поиска документов (имя, тип, дата, размер) в поле "Фильтрация"

    Use case #2: пользователь загружает документ в приложение, затем в базу данных
    - если пользователь находится не на вкладке "Создать", то во вкладке "Хранилище" находится кнопка "Загрузить документ", при ее нажатии открывается вкладка "Создать"
    - во вкладке "Создать" внутри панели "Файл" расположена кнопка "Выбрать файл", при нажатии на которую, открывается окно файловой системы
    - после выбора файла, происходит валидация (проверка): формат, размер, права доступа к документу
    - при провале валидации, пользователю выводится сообщение с данными об ошибке
    - при успешной валидации, в панеле "Файл" выводится информация о загруженном файле и отображается панель "Шифрование"
    - в панели "Шифрование" есть выпадающее меню "Метод" для выбора метода шифрования файла, а так же поле "Ключ" для ввода ключак зашифрованному файлу
    - после заполнения всех полей на вкладке разблокируется кнопка "Зашифровать файл", при нажатии начинается процесс шифрования докумеента
    - по окончании процесса шифрования выводится информация о результате операции (успех или провал)
    - в случае успеха отображается последняя панель "Загрузка в базу данных", где в поле "Имя" можно задать имя документа в БД, в поле "ID" указать Id документа в БД, а поле "Пароль" задать дополнительный пароль к документу
    - для загрузки документа в БД пользователь должен нажать на кнопку "Загрузить". далее отобразится результат операции

    Use case #3: пользователь загружает документ в приложение, затем отменяет операцию
    - в случае если пользователь дошел до этапа 7 UC #2, в верхней части вкладки отобразится кнопка "Отмена", которая удалит локально зашифрованный файл и отчистит все панели
    - если программа была завершена до совершения каких-либо действий со стороны пользователя после шага 7 UC #2, файл удалится автоматически

    Use case #4: пользователь успешно загружает документ из приложения
    - в продолжение UC #1. в панеле "Документы" пользователь кликает на документ и открывается окно "Просмотр", где отображены данные о документе
    - в данном окне есть кнопка "Скачать". при ее нажатии появится форма, где нужно ввести ключ и пароль (при наличии) к данному документу
    - при успешном вводе ключа, файл передается клиенту из БД, затем расшифровывается на стороне пользователя
    - после расшифровки выводится сообщение с результатом процесса и, при успехе, файл сохраняется в выбранной директории

    Use case #5: пользователь не смог загрузить документ: неверные данные или ошибка расшифровки
    - в продолжение этапа 2 UC #4. если пользователь ввел неверные данные, то выводится сообщение об ошибке и предлагается попробовать еще раз. однако, в случае 5 неудачных попыток, файл будет временно заблокирован
    - если файл был получен из БД, но расшифровка файла провалилась, пользователю выводится сообщение об ошибке и предлагается удалить файл из хранения

    Use case #6: пользователь удаляет документ из хранилища
    - в продолжение UC #1. в панеле "Документы" пользователь кликает на документ и открывается окно "Просмотр", где есть кнопка "Удалить"
    - при нажатии на кнопку "Удалить" необходимо ввести пароль (если к документу залан пароль) и подтвердить операцию
    - по окончании операции, отобразится результат об успехе

    Use case #7: пользователь меняет шифр для докумета в хранилище
    - в продолжение UC #1. в панеле "Документы" пользователь кликает на документ и открывается окно "Просмотр", где есть кнопка "Изменить ключ"
    - при нажатии на кнопку "изменить ключ" необходимо ввести старый ключ к документу и пароль (если к документу залан пароль) и подтвердить операцию
    - по окончании операции, отобразится результат об успехе

    Use case #8: пользователь меняет учебные данные (логин, пароль)
    - пользователь переходит во вкладку "Профиль", в панеле "Данные" отображен ID и логин пользователя, а также есть кнопка "Изменить данные"
    - при нажатии на кнопку "Изменить данные" откроется окно с полями "Старый пароль", "Новый логин" и "Новый пароль". для изменения любого поля необходимо ввести текущий пароль
    - при правильном вводе пароля, данные будут обновлены, пользователю выведется сообщение об успехе, иначе об ошибке

    Use case #9: пользователь создает профиль
    - перед пользователем открывается окно "Регистрация", где есть поля "Логин" и "Пароль"
    - при вводе учетных данных разблокируется кнопка "Зарегистрироваться", при нажатии на которую в БД отправится запрос на регистрацию
    - в случае успеха, пользователь автоматически авторизируется в приложение

    Use case #10: пользователь удаляет профиль
    - пользователь переходит во вкладку "Профиль", внизу находится кнопка "Удалить профиль", при нажатии на которую необходимо повторно ввести пароль и подтвердить действие
    - далее пользователя возвращает на окно "Авторизация"


## **Детали реализации:**
    
### ГРАФИКА
Для создания UI использовался фреймворк JavaFX.
JavaFX представляет инструментарий для создания кроссплатформенных графических приложений на платформе Java.
JavaFX позволяет создавать приложения с богатой насыщенной графикой благодаря использованию аппаратного ускорения графики и возможностей GPU.
С помощью JavaFX можно создавать программы для различных операционных систем: Windows, MacOS, Linux, Android,
iOS и для самых различных устройств: десктопы, смартфоны, планшеты, встроенные устройства, ТВ.
Приложение на JavaFX будет работать везде, где установлена исполняемая среда Java (JRE).
JavaFX предоставляет большие возможности по сравнению с рядом других подобных платформ, в частности, по сравнению со Swing.
Это и большой набор элементов управления, и возможности по работе с мультимедиа, двухмерной и трехмерной графикой,
декларативный способ описания интерфейса с помощью языка разметки FXML, возможность стилизации интерфейса с помощью CSS, интеграция со Swing и многое другое.

### ШИФРОВАНИЕ
Java для работы с криптографией предлагает Java Cryptography Architecture (JCA).
Данная архитектура содержит API (т.е. некоторый набор интерфейсов) и провайдеры (которые их реализуют).
То есть платформа Java предоставляет набор встроенных провайдеров, которые при необходимости можно дополнить.
Зарегистрировать сторонний провайдер очень просто. Подключим один из самых известных провайдеров — BouncyCastle.

### БАЗА ДАННЫХ
MongoDB — система управления базами данных, которая работает с документоориентированной моделью данных. В отличие от реляционных СУБД, MongoDB не требуются таблицы, схемы или отдельный язык запросов. Информация хранится в виде документов либо коллекций.
Разработчики позиционируют продукт как промежуточное звено между классическими СУБД и NoSQL. MongoDB не использует схемы, как это делают реляционные базы данных, что повышает производительность всей системы.

У MongoDB есть ряд свойств, которые выделяют ее на фоне других продуктов:
- Кроссплатформенность. СУБД разработана на языке программирования С++, поэтому с легкостью интегрируется под любую операционную систему (Windows, Linux, MacOS и др.).
- Формат данных. MongoDB использует собственный формат хранения информации — Binary JavaScript Object Notation (BSON), который построен на основе языка JavaScript.
- Документ. Если реляционные БД используют строки, то MongoDB — документы, которые хранят значения и ключи.
- Вместо таблиц MongoDB использует коллекции. Они содержат разные типы наборов данных
- Репликация. Система хранения информации в СУБД представлена узлами. Существует один главный и множество вторичных. Данные реплицируются между точками. Если один первичный узел выходит из строя, то вторичный становится главным.
- Индексация. Технология применяется к любому полю в документе на усмотрение пользователя. Проиндексированная информация обрабатывается быстрее.
- Для сохранения данных большого размера MongoDB использует собственную технологию GridFS, состоящую из двух коллекций. В первой (files) содержатся имена файлов и метаданные по ним. Вторая (chunks) сохраняет сегменты информации, размер которых не превышает 256 Кб.
- СУБД осуществляет поиск по специальным запросам. Например, пользователь может создать диапазонный запрос и мгновенно получить ответ.
- Балансировщик нагрузки используется в СУБД не только для распределения нагрузки между разными базами данных, но и для горизонтального масштабирования. Сегменты БД распределяются по разным узлам, что повышает производительность. При этом базы данных, расположенные на разных узлах, синхронизированы между собой и обеспечивают целостность информации для клиента.
- MongoDB может поставляться для конечного клиента как облачное решение.
СУБД используют для хранения событий в системе (логирование), записи информации с датчиков мониторинга на предприятии, а также в сфере электронной коммерции и мобильных приложений. Часто MongoDB применяют как хранилище в сфере машинного обучения и искусственного интеллекта.
MongoDB относится к классу NoSQL СУБД и работает с документами, а не с записями. Это кроссплатформенный продукт, который легко внедряется в любую операционную систему. Ряд уникальных особенностей позволяет использовать СУБД под определённые задачи, в которых она обеспечивает максимальную производительность и надежность.