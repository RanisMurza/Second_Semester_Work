package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestTwoThreeTree {
    public static void main(String[] args) throws IOException {
        //создаем список из 10_000 целых чисел
        List<Integer> dataInteger = new ArrayList<>();
        //генерируем в список рандомные числа
        for (int i = 0; i < 10_000; i++) {
            Integer randomNumber = (int)(-10_000 + Math.random() * 20_000);
            //ищем такое число, которого нет в списке
            while(dataInteger.contains(randomNumber)){
                randomNumber = (int)(-10_000 + Math.random() * 10_000);
            }
            dataInteger.add(randomNumber);
        }
        //создаем 2-3 дерево, с которым будем в дальнейшем работать
        TwoThreeTree twoThreeTree = new TwoThreeTree();
        //добавление
        testInsert(twoThreeTree, dataInteger);
        //поиск
        testSearch(twoThreeTree, dataInteger);
        //удаление
        testRemove(twoThreeTree, dataInteger);

    }
    public static void testInsert(TwoThreeTree tree, List<Integer> data) throws IOException {
        File results = new File("C:/University/2_semester/АиСД/semester_work_second/results/result_insert.txt");
        results.createNewFile();

        FileWriter writer = new FileWriter("C:/University/2_semester/АиСД/semester_work_second/results/result_insert.txt");
        String headlines = String.format("%-10s %-30s %-30s %-10s\n", "№", "Добавленное число", "Время работы в наносекундах", "Количество итераций");
        writer.write(headlines);

        long averageTime; //среднее время добавления
        long averageIterations; //среднее количество операций
        long sumTime = 0; //сумма времен добавления
        long sumIterations = 0; //сумма количества итераций

        int insertedNumber; //текущее число

        for(int i = 0; i < data.size(); i++){
            insertedNumber = data.get(i);
            long start = System.nanoTime();
            tree.insert(insertedNumber);
            long finish = System.nanoTime();
            //время в наносекундах
            long time = finish - start;

            sumTime += time;
            sumIterations += tree.getCountInsertIterations();

            String page = String.format("%-10s %-30s %-30s %-10s\n", i + 1, insertedNumber ,time, tree.getCountInsertIterations());
            writer.write(page);
        }
        averageTime = sumTime / 10_000;
        averageIterations = sumIterations / 10_000;
        writer.write("Среднее время добавления: " + averageTime + " наносекунд\n");
        writer.write("Среднее количество итераций при добавлении: " + averageIterations + " итераций\n");
        writer.close();
    }
    public static void testSearch(TwoThreeTree tree, List<Integer> data) throws IOException {
        File results = new File("C:/University/2_semester/АиСД/semester_work_second/results/result_search.txt");
        results.createNewFile();

        FileWriter writer = new FileWriter("C:/University/2_semester/АиСД/semester_work_second/results/result_search.txt");
        String headlines = String.format("%-30s %-30s %-30s %-10s\n", "Номер разыскиваемого элемента", "Добавленное число", "Время работы в наносекундах", "Количество итераций");
        writer.write(headlines);

        long averageTime; //среднее время поиска
        long averageIterations; //среднее количество операций
        long sumTime = 0; //сумма времен поиска
        long sumIterations = 0; //сумма количества итераций

        int searchedNumber;
        //ищем 100 чисел
        for(int i = 1; i <= 100; i++){
            //число, которое будем искать в дереве
            searchedNumber = data.get((int)(Math.random() * 10000 - 1));
            long start = System.nanoTime();
            tree.insert(searchedNumber);
            long finish = System.nanoTime();
            long time = finish - start; //время работы методы

            sumTime += time;
            sumIterations += tree.getCountSearchIterations();

            String dates = String.format("%-30s %-30s %-30s %-30s\n", i, searchedNumber ,time, tree.getCountSearchIterations());
            writer.write(dates);
        }
        averageTime = sumTime / 100;
        averageIterations = sumIterations / 100;
        writer.write("Среднее время поиска: " + averageTime + " наносекунд\n");
        writer.write("Среднее количество итераций при поиске: " + averageIterations + " итераций\n");
        writer.close();
    }
    public static void testRemove(TwoThreeTree tree, List<Integer> data) throws IOException {
        File results = new File("C:/University/2_semester/АиСД/semester_work_second/results/result_remove.txt");
        results.createNewFile();

        FileWriter writer = new FileWriter("C:/University/2_semester/АиСД/semester_work_second/results/result_remove.txt");
        String headlines = String.format("%-30s %-30s %-30s %-10s\n", "Номер удаленного элемента", "Удаленное число", "Время работы в наносекундах", "Количество итераций");
        writer.write(headlines);

        long averageTime; //среднее время удаления
        long averageIterations; //среднее количество операций
        long sumTime = 0; //сумма времен удаления
        long sumIterations = 0; //сумма количества итераций

        int removedNumber;
        for(int i = 0; i < 1000; i++){
            removedNumber = (int)(Math.random()*(10_000 - i));
            data.remove(removedNumber);
            long start = System.nanoTime();
            tree.remove(removedNumber);
            long finish = System.nanoTime();
            long time = finish - start; //время работы методы

            sumTime += time;
            sumIterations += tree.getCountRemoveIterations();

            String dates = String.format("%-30s %-30s %-30s %-30s\n", i + 1, removedNumber ,time, tree.getCountRemoveIterations());
            writer.write(dates);
        }
        averageTime = sumTime / 1000;
        averageIterations = sumIterations / 1000;
        writer.write("Среднее время удаления: " + averageTime + " наносекунд\n");
        writer.write("Среднее количество итераций при удалении: " + averageIterations + " итераций\n");
        writer.close();
    }
}
