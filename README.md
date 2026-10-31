# DI Container

Учебный DI-контейнер на Java.

## Возможности

- Сканирование классов с аннотацией `@Component`
- Автоматический инжект зависимостей через `@Inject`
- Поддержка цепочек зависимостей
- Повторный сканирование пакетов

## Использование

```java
// 1. Аннотируйте классы
@Component
public class UserService { }

@Component
public class UserController {
    @Inject
    private UserService userService;
}

// 2. Создайте контекст и просканируйте
ApplicationContext context = new ApplicationContext();
context.scan(UserService.class, UserController.class);

// 3. Получите бин
UserController controller = context.getBean(UserController.class);
```

## Структура

| Класс | Назначение |
|-------|-----------|
| `@Component` | Аннотация для маркировки бин-ов |
| `@Inject` | Аннотация для поля, которое нужно заинжектить |
| `ApplicationContext` | Контейнер: создаёт бины и связывает зависимости |

## Тесты

```bash
mvn test
```
