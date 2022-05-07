package stream.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
       // printTheMostFrequentWordInFile(Paths.get("text.txt"));
        collectExamples();

    }

    public static void collectExamples() {
        List<Product> productList = Arrays.asList(new Product(23, "potatoes"),
                new Product(14, "orange"), new Product(13, "lemon"),
                new Product(23, "bread"), new Product(13, "sugar"));
        // ================================================================================================
        List<String> collectorCollection =
                productList.stream().map(Product::getName).collect(Collectors.toList());
        // ================================================================================================
        String listToString = productList.stream().map(Product::getName)
                .collect(Collectors.joining(", ", "[", "]"));
        //System.out.println(listToString);
        // ================================================================================================
        double averagePrice = productList.stream()
                .collect(Collectors.averagingInt(Product::getPrice));
       // System.out.println(averagePrice);
        // ================================================================================================
        Integer summingPrice  = productList.stream().collect(Collectors.summingInt(Product::getPrice));
        //System.out.println(summingPrice);
        // ================================================================================================
        IntSummaryStatistics statistics = productList.stream()
                .collect(Collectors.summarizingInt(Product::getPrice));
        // System.out.println(statistics.toString());
        // ================================================================================================
        Map<Integer, List<Product>> collectorMapOfLists = productList.stream()
                .collect(Collectors.groupingBy(Product::getPrice));
        //System.out.println(collectorMapOfLists);
        // ================================================================================================
        Map<Boolean, List<Product>> mapPartioned = productList.stream()
                .collect(Collectors.partitioningBy(element -> element.getPrice() > 15));
        //System.out.println(mapPartioned);
        // ================================================================================================
        Set<Product> unmodifiableSet = productList.stream()
                .collect(Collectors.collectingAndThen(Collectors.toSet(),
                        s -> Collections.unmodifiableSet(s)));
        System.out.println(unmodifiableSet);
        //В данном конкретном случае Collector преобразовал поток в Set, а затем создал из него неизменяемый Set.
    }
    public static void howItWorks() {
        List<String> list = Arrays.asList("abc1", "abc2","abc3","sdf2", "asfsdff2");
        list.stream().limit(2).filter(element -> {
            System.out.println("filter() was called for " + element);
            return element.contains("2");
        }).map(element -> {
            System.out.println("map() was called for " + element);
            return element.toUpperCase();
        }).forEach(System.out::println);
    }
    public static void intermediateAndTerminateOperations() {
        //We can instantiate a stream, and have an accessible reference to it, as long as only intermediate operations are called. Executing a terminal operation makes a stream inaccessible.//
        //To demonstrate this, we will forget for a while that the best practice is to chain the sequence of operation. Besides its unnecessary verbosity, technically the following code is valid:
        Stream<String> stream =
                Stream.of("a", "b", "c").filter(element -> element.contains("b"));
        Optional<String> anyElement = stream.findAny();
        //However, an attempt to reuse the same reference after calling the terminal operation will trigger the IllegalStateException:
        Optional<String> firstElement = stream.findFirst();
        //As the IllegalStateException is a RuntimeException, a compiler will not signalize about a problem. So it is very important to remember that Java 8 streams can't be reused.
//        This kind of behavior is logical. We designed streams to apply a finite sequence of operations to the source of elements in a functional style, not to store elements.
        // So to make the previous code work properly, some changes should be made:

        List<String> elements =
                Stream.of("a", "b", "c").filter(element -> element.contains("b"))
                        .collect(Collectors.toList());
        Optional<String> anyElement2 = elements.stream().findAny();
        Optional<String> firstElement2 = elements.stream().findFirst();

    }

    public static void generateRandomNumbersForList() {
        Random random = new Random();
        IntStream ints = random.ints(15,20,30);
        List<Integer> collect = ints.boxed().collect(Collectors.toList());
        System.out.println(collect);
    }
    public static void generateAndIterateExample() {
        //The generate() method accepts a Supplier<T> for element generation. As the resulting stream is infinite,
        // the developer should specify the desired size, or the generate() method will work until it reaches the memory limit:
        List<String> collect = Stream.generate(() -> "element").limit(10).collect(Collectors.toList());
        System.out.println(collect);
        //Another way of creating an infinite stream is by using the iterate() method:
        List<Integer> collect1 = Stream.iterate(10, n -> n + 2).limit(15).collect(Collectors.toList());
        System.out.println(collect1);
        //The first element of the resulting stream is the first parameter of the iterate() method.
        // When creating every following element, the specified function is applied to the previous element.
        // In the example above the second element will be 12.
    }

    public static void someExamples() {
        Stream<String> builder = Stream.<String>builder().add("S").add("R").add("F").add("W").build();
        List<String> collect = builder.collect(Collectors.toList());
        System.out.println(collect);
    }

    public static void findNames() {
        List<String> list = new ArrayList<>(Arrays.asList("John", "Bane", "Adam", "Jake"));
        list.stream().filter((s) -> s.charAt(0) == 'J').forEach(System.out::println); // or

        Predicate<String> predicate = s -> s.charAt(0) == 'J';
        list.stream().filter(predicate).forEach(System.out::println);
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
    public static void flatMapEx() {
        try {
            Files.lines(Paths.get("text.txt")) // Wrong variant
                    .map(line -> line.split("\\s"))
                    .distinct()
                    .forEach(System.out::println);
            System.out.println("-----------------");
            String str = Files.lines(Paths.get("text.txt"))
                    .map(line -> line.split("\\s"))// создается четыре массива
                    .flatMap((arr) -> Arrays.stream(arr))// из каждого массива делаем стрим и потом их сжимаем в одну
                    .distinct()
                    .collect(Collectors.joining(",", "Unique words:", "."));
            System.out.println(str);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printTheMostFrequentWordInFile(Path path) {
        try {
            Files.lines(path)
                    .map(s -> s.split("\\s")) // тут делим по линиям
                    .flatMap(Arrays::stream)
                    .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                    .entrySet()
                    .stream()
                    .sorted((e1, e2) -> e2.getValue().intValue() - e1.getValue().intValue())
                    .limit(1)
                    .findFirst().ifPresent(s -> System.out.println("The most frequent word: " + s));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void countWordsInFile(Path path) {
        try {
            Map<String, Long> map = Files.lines(path)
                    .map(line -> line.split("\\s"))
                    .flatMap(Arrays::stream)
                    .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
            System.out.println(map);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void intStreamEx() {
        IntStream intStream = IntStream.of(10,20,30,40);
        List<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,45));
        list.stream().mapToInt((n) -> n).forEach(System.out::println);
        IntStream.range(0, 10).filter((n) -> n % 2 == 0).forEach(System.out::println);
    }

    public static void reduceEx() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4,5));
        int sum = 0;
        for(int i : list) {
            sum += 0;
        }

        int result = list.stream().reduce(0, (a,b) -> a + b);
    }

    public static void findAnyEx() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1,2,13,4,15));

        list.stream().filter((n) -> n > 10).findAny().ifPresent(System.out::println);
        Optional<Integer> optional = list.stream().filter((n) -> n > 10).findAny();
        if(optional.isPresent()) {
            System.out.println(optional.get());
        }
    }

    public static void mapEx() {
        Stream.of(1,2,3).map((n) -> Math.pow(n, 3)).forEach(System.out::println); // 1 variant
        Function<Integer, Integer> cube = (n) -> n * n * n;
        Stream.of(1,2,3).map(cube).forEach(System.out::println); // 2 variant

        Function<Integer, String> convert = (i) -> String.valueOf(i);
        Stream.of(1,2,3,4).map(convert).forEach(System.out::println);

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
        Map<Integer, List<String>> map = Stream.of("A","BB","AA","B","C","EE","NNN","Q").collect(Collectors.groupingBy((s) -> s.length()));
        Map<Integer, Long> map1 = Stream.of("A","BB","AA","B","C","EE","NNN","Q").collect(Collectors.groupingBy((s) -> s.length(), Collectors.counting()));

        System.out.println(map1);
    }

    public static void peekExample() {
        Stream.of("one", "two", "three", "four")
                .filter(e -> e.length() > 3)
                .peek(e -> System.out.println("Filtered value: " + e))
                .map(String::toUpperCase)
                .peek(e -> System.out.println("Mapped value: " + e))
                .collect(Collectors.toList());
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

    public static void myOwnFilterEx() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1,2,3,4,5,6));
        System.out.println(myOwnFilter(list, i -> i % 2 == 0));
        System.out.println(myOwnFilter(list, i -> i % 2 != 0));
        List<String> list2 = new ArrayList<>(Arrays.asList("cool", "bool", "go"));
        System.out.println(myOwnFilter(list2, s -> s.length() > 3));
    }

    public static <T> List<T> myOwnFilter(List<T> list, Predicate<T> predicate) {
        List<T> copy = new ArrayList<>(list);
        Iterator<T> iterator = copy.iterator();
        while (iterator.hasNext()) {
            T o = iterator.next();
            if(!predicate.test(o)) {
                iterator.remove();
            }
        }
        return copy;

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

    private static class Product {
        private int price;
        private String name;

        public Product(int price, String name) {
            this.price = price;
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Product{" +
                    "price=" + price +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
