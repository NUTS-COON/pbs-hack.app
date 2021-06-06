# Описание проекта

Концепт банковского Android приложения, использующего микросервисную архитектуру. Каждый сервис разрабатывается как отдельный Android модуль (логически и функционально законченный компонент) и подключается как maven пакет. Преимущества такого подхода:
- разработка и тестирование модулей независимо разными командами в разных репозиториях
- быстрое обновление версий модуля или откат в случае ошибок как обычного maven пакета
- разработка модулей как обычных Android приложений
- сборка и развёртывание конечного приложения может происходить автоматически, как и любого другого
- возможность создания одного модуля с общими стилями, позволяя лёгко обновить стили по всему приложению

Запуск activity из других модулей, передача в них данных и получение результата делается с помощью стандартного механизма Android - Intent'ов. Для получения более сложных данных может быть реализован общий механизм рассылки сообщений внутри приложения.

В этом проекте подключено 3 модуля из других репозиториев: [кредиты](https://github.com/NUTS-COON/pbs-hack.credit), [сторисы](https://github.com/NUTS-COON/pbs-hack.storis), [настройки](https://github.com/NUTS-COON/pbs-hack.settings/tree/master). Каждый модуль собран в aar файл и загружен на [JFrog Platform](https://jfrog.com) как maven пакет. Модуль стрессов содержит кастомный контрол, который можно инициализировать в нужный момент и узнать количество сторисов.

# Настройки проекта

Для подключения модулей, загруженных на [JFrog Platform](https://jfrog.com), в файле build.gradle добавлен maven репозиторий 
`https://nutscoon.jfrog.io/artifactory/default-maven-local`. В файле app/build.gradle в секции dependencies модули подключаются следующим образом  
```
implementation 'ru.nutscoon.psb:settings:1.0.0'
implementation 'ru.nutscoon.psb:credit:1.0.0'
implementation 'ru.nutscoon.psb:storis:1.0.0'
```

# Найтройки модуля

Для сборки и загрузки модуля в maven репозиторий в build.gradle добавлена зависимость `classpath "org.jfrog.buildinfo:build-info-extractor-gradle:latest.release"`, добавлены плагины com.jfrog.artifactory и maven-publish  
```
allprojects {
    apply plugin: 'com.jfrog.artifactory'
    apply plugin: 'maven-publish'
    repositories {
        google()
        mavenCentral()
    }
}
```
и секции
```
project('PROJECT_NAME') {
    artifactoryPublish.dependsOn('build')
    publishing {
        publications {
            aar(MavenPublication) {
                groupId 'GROUP_ID'
                artifactId 'ARTIFACT_ID'
                version 'VERSION'
                artifact("$buildDir/outputs/aar/${project.getName()}-debug.aar")
            }
        }
    }

    artifactoryPublish {
        publications(publishing.publications.aar)
    }
}

artifactory {
    contextUrl = 'https://nutscoon.jfrog.io/artifactory'
    publish {
        repository {
            repoKey = 'default-maven-local'
            username = "USER_NAME"
            password = "PASSWORD"
        }
        defaults {
            publishArtifacts = true
            publishPom = true
        }
    }
}
```
В *contextUrl* указан URL maven репозитория, куда будут выгружаться пакеты. Также здесь для каждого нового модуля нужно указать:
- PROJECT_NAME - какой модуль будет выгружаться в репозиторий
- GROUP_ID, ARTIFACT_ID, VERSION - название и версия maven пакета для подключения в другие проекты
- USER_NAME и PASSWORD - учётные данные от репозитория, куда будут выгружаться пакеты
