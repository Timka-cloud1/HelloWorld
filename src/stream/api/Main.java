package stream.api;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        countExample();

    }

    public static void example1() {
        List<Person> persons = new ArrayList<>(Arrays.asList(
                new Person("Bob1", 35, Person.Position.MANAGEMENT),
                new Person("Bob2", 54, Person.Position.DIRECTOR),
                new Person("Bob3", 44, Person.Position.ENGINEER),
                new Person("Bob4", 33, Person.Position.ENGINEER),
                new Person("Bob5", 21, Person.Position.DIRECTOR),
                new Person("Bob6", 57, Person.Position.MANAGEMENT),
                new Person("Bob7", 45, Person.Position.ENGINEER)
        ));
        List<Person> engineers = new ArrayList<>();
        for(Person p : persons) {
            if(p.position == (Person.Position.ENGINEER)) {
                engineers.add(p);
            }
        }
      //  System.out.println(engineers);
        engineers.sort(Comparator.comparingInt(o -> o.age));
      //  System.out.println(engineers);
        List<String> engineersName = new ArrayList<>();
        for(Person p : engineers) {
            engineersName.add(p.name);
        }
        System.out.println(engineersName);
        // with stream api
        List<String> names = persons.stream().filter((p) -> p.position == Person.Position.ENGINEER).sorted(Comparator.comparingInt(p -> p.age)).map((p) -> p.name).collect(Collectors.toList());
        System.out.println(names);


    }

    public static void example2() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4,5,6));
        list.stream().filter((i) -> i % 2 == 0).map((i) -> i * i).map((i) -> {
            StringBuilder out = new StringBuilder();
            for (int j = 0; j < i; j++) {
                out.append("A");
            }
            return out;
        }).limit(2).sorted(Comparator.reverseOrder()).forEach(System.out::println);
    }

    public static void filterEx() {
        Stream.of(1,2,3,4,5,6,7,8).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer % 2 == 0;
            }
        }).forEach(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                integer += 10;
                System.out.println(integer);
            }
        });

        Stream.of(1,2,3,4,5,6,7,8).filter((n) -> n % 2 == 0).forEach((n) -> {
            n += 10;
            System.out.println(n);
        });
    }

    public static void countExample() {
        Map<Integer, Long> map = Stream.of("A","BB","AA","B","C","EE","NNN","Q").collect(Collectors.groupingBy((s) -> s.length(), Collectors.counting()));
        System.out.println(map);
    }
    public static void matchEx() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1,3,14,5,4,6));
        boolean ok = true;
        for(Integer i : list) {
            if(i >= 10) {
                ok = false;
                break;
            }
        }

        System.out.println(list.stream().allMatch((n) -> n < 10));
        System.out.println(list.stream().anyMatch((n) -> n == 4));
        System.out.println(list.stream().noneMatch((n) -> n == 2));
    }


    private static class Person {
        enum Position {
            ENGINEER, DIRECTOR, MANAGEMENT
        }
        private String name;
        private int age;
        private Position position;


        public Person(String name, int age, Position position) {
            this.name = name;
            this.age = age;
            this.position = position;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", position=" + position +
                    '}';
        }
    }
}
