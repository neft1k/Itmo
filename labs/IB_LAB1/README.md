# Лабораторная работа № 1 — Информационная безопасность

Защищённый REST API личных заметок на Java 17 и Spring Boot. База данных — PostgreSQL в Docker. Аутентификация через JWT, пароли хранятся в виде bcrypt-хэшей.

- `POST /auth/login` — вход и получение токена.
- `GET /api/data` — получение своих заметок.
- `POST /api/notes` — создание заметки.

## Запуск

Нужны JDK 17+ и запущенный Docker Desktop. Все команды выполняются из `labs/IB_LAB1`.

В папке проекта должен быть локальный `.env` (в Git не добавляется):

```properties
DB_NAME=notes
DB_USER=notes
DB_PORT=5432
DB_PASSWORD=ваш_пароль_базы
APP_USERNAME=student
APP_PASSWORD=ваш_пароль_от_12_символов
JWT_SECRET=случайный_ключ_в_Base64
```

Для `JWT_SECRET` сгенерируйте значение командой `openssl rand -base64 32`. Если `.env` уже настроен, используйте его. Приложение читает файл автоматически.

```bash
docker compose up -d --wait
./mvnw spring-boot:run
```

API доступно на `http://localhost:8080`. Для входа используйте `APP_USERNAME` и `APP_PASSWORD`.

Остановка приложения — `Ctrl+C`, базы — `docker compose stop db`. Данные базы сохраняются в Docker volume.
