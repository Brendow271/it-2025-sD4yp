package com.harmony.config;

import com.harmony.entity.UserPhoto;
import com.harmony.repository.UserPhotoRepository;
import com.harmony.service.SwipeService;
import com.harmony.service.UserAuthService;
import com.harmony.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class MockDataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MockDataInitializer.class);

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserPhotoRepository userPhotoRepository;

    @Autowired
    private SwipeService swipeService;

    @Override
    public void run(String... args) {
        logger.info("Начало инициализации моковых данных...");

        try {
            createMockUsers();
            logger.info("Моковые пользователи успешно созданы");
        } catch (Exception e) {
            logger.error("Ошибка при создании моковых данных: {}", e.getMessage(), e);
        }
    }

    private void createMockUsers() {
        String password = "password";

        MockUser[] mockUsers = {
            new MockUser("Алексей Петров", "alexey.petrov@example.com", 25,
                new String[]{"Рок", "Метал", "Альтернатива"},
                new String[]{"Гитара", "Электрогитара"},
                "Москва",
                "Гитарист, играю в рок-группе. Ищу единомышленников для совместных проектов.",
                "/images/1/04032e08-f4ae-4ab8-9ff2-f7a3343debe5.png"),

            new MockUser("Мария Иванова", "maria.ivanova@example.com", 23,
                new String[]{"Поп", "R&B", "Электронная музыка"},
                new String[]{"Вокал", "Синтезатор"},
                "Санкт-Петербург",
                "Вокалистка и клавишница. Люблю экспериментировать с электронной музыкой.",
                "/images/1/0874e2e5-39ba-4031-95cd-76d4bfedd251.png"),

            new MockUser("Дмитрий Смирнов", "dmitry.smirnov@example.com", 28,
                new String[]{"Джаз", "Блюз", "Соул"},
                new String[]{"Саксофон", "Кларнет"},
                "Казань",
                "Джазовый саксофонист. Ищу музыкантов для джазового ансамбля.",
                "/images/1/0d5b2b1f-0e0d-41e0-9a40-5e0867707eb8.png"),

            new MockUser("Анна Козлова", "anna.kozlova@example.com", 22,
                new String[]{"Классическая музыка", "Инструментальная музыка"},
                new String[]{"Скрипка", "Пианино"},
                "Екатеринбург",
                "Классическая скрипачка. Играю в оркестре, ищу камерные ансамбли.",
                "/images/1/629aa058-5204-4de5-8bcf-bb570adb5c9d.jpg"),

            new MockUser("Иван Новиков", "ivan.novikov@example.com", 26,
                new String[]{"Хип-хоп", "Рэп", "Электронная музыка"},
                new String[]{"Вокал", "DJ-оборудование"},
                "Новосибирск",
                "Рэпер и диджей. Ищу продюсеров и других артистов для коллабораций.",
                "/images/1/04032e08-f4ae-4ab8-9ff2-f7a3343debe5.png"),

            new MockUser("Елена Волкова", "elena.volkova@example.com", 24,
                new String[]{"Инди", "Альтернатива", "Рок"},
                new String[]{"Бас-гитара", "Вокал"},
                "Краснодар",
                "Басистка в инди-группе. Люблю экспериментировать со звуком.",
                "/images/1/0874e2e5-39ba-4031-95cd-76d4bfedd251.png"),

            new MockUser("Сергей Лебедев", "sergey.lebedev@example.com", 30,
                new String[]{"Джаз", "Фанк", "Соул"},
                new String[]{"Барабаны", "Бонго"},
                "Ростов-на-Дону",
                "Профессиональный барабанщик. Играю в джаз-бэнде, ищу новые проекты.",
                "/images/1/0d5b2b1f-0e0d-41e0-9a40-5e0867707eb8.png"),

            new MockUser("Ольга Соколова", "olga.sokolova@example.com", 27,
                new String[]{"Поп", "Фолк", "Акустическая музыка"},
                new String[]{"Гитара", "Укулеле", "Вокал"},
                "Нижний Новгород",
                "Акустическая певица и гитаристка. Пишу свои песни, ищу соавторов.",
                "/images/1/629aa058-5204-4de5-8bcf-bb570adb5c9d.jpg")
        };

        Long[] userIds = new Long[mockUsers.length];

        for (int i = 0; i < mockUsers.length; i++) {
            MockUser mockUser = mockUsers[i];
            
            if (!userAuthService.isEmailExists(mockUser.email)) {
                var user = userAuthService.registerUser(mockUser.name, mockUser.email, password);
                userIds[i] = user.getUserId();
                
                userInfoService.updateInfo(
                    userIds[i],
                    mockUser.age,
                    mockUser.genres,
                    mockUser.instruments,
                    mockUser.location,
                    mockUser.about
                );

                UserPhoto photo = new UserPhoto();
                photo.setUserId(userIds[i]);
                photo.setImageUrl(mockUser.imageUrl);
                userPhotoRepository.save(photo);

                logger.info("Создан пользователь: {} ({})", mockUser.name, mockUser.email);
            } else {
                var existingUser = userAuthService.getUserByEmail(mockUser.email);
                userIds[i] = existingUser.getUserId();
                logger.info("Пользователь уже существует: {} ({})", mockUser.name, mockUser.email);
            }
        }

        createMockSwipes(userIds);
    }

    private void createMockSwipes(Long[] userIds) {
        Integer[][] swipes = {
            {0, 1, 1, null},
            {0, 2, 1, 0},
            {0, 3, 0, null},
            {1, 2, 1, 1},
            {1, 4, 1, null},
            {1, 5, 0, null},
            {2, 3, 1, 1},
            {2, 6, 1, null},
            {3, 4, 0, null},
            {3, 7, 1, 1},
            {4, 5, 1, 1},
            {4, 6, 1, null},
            {5, 6, 1, 0},
            {5, 7, 1, null},
            {6, 7, 1, 1}
        };

        for (Integer[] swipe : swipes) {
            int user1Index = swipe[0];
            int user2Index = swipe[1];
            Boolean decision1 = swipe[2] == 1;
            Integer decision2Value = swipe[3];

            Long userId1 = userIds[user1Index];
            Long userId2 = userIds[user2Index];

            try {
                swipeService.createSwipe(userId1, userId2, decision1);
                
                if (decision2Value != null) {
                    Boolean decision2 = decision2Value == 1;
                    swipeService.createSwipe(userId2, userId1, decision2);
                }
            } catch (Exception e) {
                logger.debug("Свайп уже существует или ошибка: {} -> {}", userId1, userId2);
            }
        }

        logger.info("Моковые свайпы созданы");
    }

    private static class MockUser {
        String name;
        String email;
        Integer age;
        String[] genres;
        String[] instruments;
        String location;
        String about;
        String imageUrl;

        MockUser(String name, String email, Integer age, String[] genres,
                String[] instruments, String location, String about, String imageUrl) {
            this.name = name;
            this.email = email;
            this.age = age;
            this.genres = genres;
            this.instruments = instruments;
            this.location = location;
            this.about = about;
            this.imageUrl = imageUrl;
        }
    }
}

