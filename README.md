# Working with Hazelcast

Для роботи нодів я обрав метод звичайного запуску з binary.\
Для цього необхідно скачати клієнт Hazelcast (я використовував версію 5.5).\
Потім зайти в консоль/термінал, і, для старту 1-ї ноди:

```zsh
bin/hz-start
```

Для management center:

```zsh
 management-center/bin/start.sh
```

Програми в директорії com/example. Їх можливо скомпілювати та запустити як звичайні програми інструментами Java:
Приклад:\

```zsh
javac -cp "/Users/admin/Downloads/hazelcast-5.5.0/lib/hazelcast-5.5.0.jar" src/main/java/com/example/QueueProducer.java
javac -cp "/Users/admin/Downloads/hazelcast-5.5.0/lib/hazelcast-5.5.0.jar" src/main/java/com/example/QueueConsumer.java
```

```zsh
 java -cp ".:/Users/admin/Downloads/hazelcast-5.5.0/lib/*" com.example.QueueConsumer
```
