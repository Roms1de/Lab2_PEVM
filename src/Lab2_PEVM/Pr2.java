package Lab2_PEVM;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Scanner;


public class Pr2 {
    public static void deleteDir(File file) {
        if (file.isFile()) {
            System.out.println("Файл --" + file.getName() + (file.delete() ? "-- удален" : "-- не удален"));
        } else {
            File[] files_list = file.listFiles();
            if (files_list != null) {
                for (File f : files_list) {
                    deleteDir(f);
                }
            }
            System.out.println("Директория --" + file.getName() + (file.delete() ? "-- удалена" : "-- не удалена"));
        }
    }

    public static boolean searchFileorDir(String search_name, File file) {
        if (file.exists()) {
            if (file.getName().equals(search_name)) {
                return true;
            } else if (file.isDirectory()) {
                File[] list = file.listFiles();
                if (list != null) {
                    for (File f : list) {
                        if (searchFileorDir(search_name, f)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        File file = new File("");
        Scanner in = new Scanner(System.in);
        int global_ans;
        String path;


        do {
            System.out.println("Меню");
            System.out.println("    1. Выбор файла или каталога для работы");
            System.out.println("    2. Вывод абсолютного пути для текущего файла или каталога");
            System.out.println("    3. Вывод содержимого каталога");
            System.out.println("    4. Вывод информации для заданного файла");
            System.out.println("    5. Изменение имени файла или каталога");
            System.out.println("    6. Создание нового файла или каталога по заданному пути");
            System.out.println("    7. Создание копии файла по заданному пути");
            System.out.println("    8. Вывод списка файлов текущего каталога, имеющих расширение, задаваемое пользователем");
            System.out.println("    9. Удаление файла или каталога");
            System.out.println("    10. Поиск файла или каталога в выбранном каталоге");
            System.out.println("    11. Выход\n");
            System.out.print("Выберите пунк меню: ");
            global_ans = in.nextInt();

            if (!file.exists() && global_ans != 1 && global_ans != 11 && global_ans != 6) {
                System.out.println("Сначала выберите файл или каталог");
                continue;
            }

            switch (global_ans) {
                case 1:
                    in.nextLine();
                    System.out.print("Введите путь: ");
                    path = in.nextLine().trim();
                    if (path.startsWith("\"") && path.endsWith("\"")) {
                        path = path.substring(1, path.length() - 1);
                    }
                    file = new File(path);
                    break;
                case 2:
                    System.out.println("Абсолютный путь: " + file.getAbsolutePath());
                    break;
                case 3:
                    if (file.isDirectory()) {
                        System.out.println("Каталог: " + file.getName() + " {");
                        String[] dirlist = file.list();  // File[] listFiles()
                        for (int i = 0; i < dirlist.length; i++) {
                            File f = new File(file.getAbsolutePath() + "\\" + dirlist[i]);
                            if (f.isDirectory())
                                System.out.println(" --- " + f.getName() + " - каталог");
                            else
                                System.out.println(" --- " + f.getName() + " - файл");
                        }
                    } else
                        System.out.println(file.getName() + " - это не каталог");
                    System.out.println("}");
                    break;
                case 4:
                    System.out.println((file.isDirectory() ? "Каталог: " : "Файл: ") + file.getName());
                    System.out.println("Абсолютный путь: " + file.getAbsolutePath());
                    System.out.println("Родительская папка: " + file.getParent());
                    System.out.println("Размер в КБ: " + file.length() / 1000.0);
                    System.out.println(file.canWrite() ? "Подходит для записи" : "Неподходит для записи");
                    System.out.println(file.canRead() ? "Подходит для чтения" : "Неподходит для чтения");
                    long lastModified = file.lastModified();
                    Date date = new Date(lastModified);
                    System.out.println("Последнее изменение: " + date);
                    System.out.println();
                    break;
                case 5:
                    System.out.println("Текущее имя " + (file.isDirectory() ? "каталога: " : "файла: ") + file.getName());
                    in.nextLine();
                    System.out.print("Введите новое имя: ");
                    String newName = in.nextLine(); // Получение нового имени файла или каталога
                    File newFile = new File(file.getParent(), newName); // Создание нового объекта File с новым именем
                    boolean renamed = file.renameTo(newFile); // Переименование файла или каталога
                    if (renamed) {
                        System.out.println("Успешно");
                        file = newFile; // Обновляем переменную file, чтобы она указывала на переименованный файл или каталог
                    } else {
                        System.out.println("Ошибка при переименовании файла или каталога");
                    }
                    break;
                case 6:
                    in.nextLine(); // Очистка буфера ввода
                    System.out.print("Введите путь для нового файла или каталога: ");
                    String newPath = in.nextLine().trim();
                    if (newPath.startsWith("\"") && newPath.endsWith("\"")) {
                        newPath = newPath.substring(1, newPath.length() - 1);
                    }
                    File newFileOrDir = new File(newPath);
                    boolean created;
                    if (newFileOrDir.isDirectory()) {
                        created = newFileOrDir.mkdir(); // Создание нового каталога
                    } else {
                        String[] pathParts = newPath.split("\\\\");
                        String lastPart = pathParts[pathParts.length - 1];
                        if (lastPart.contains(".")) {
                            try {
                                created = newFileOrDir.createNewFile(); // Создание нового файла
                            } catch (IOException e) {
                                created = false; // Ошибка при создании файла
                            }
                        } else {
                            created = newFileOrDir.mkdir(); // Создание нового каталога
                        }
                    }
                    if (created) {
                        System.out.println("Успешно создан " + (newFileOrDir.isDirectory() ? "каталог" : "файл"));
                    } else {
                        System.out.println("Ошибка при создании " + (newFileOrDir.isDirectory() ? "каталога" : "файла"));
                    }
                    break;
                case 7:
                    in.nextLine();
                    System.out.print("Введите путь для нового файла: ");
                    String destPath = in.nextLine().trim();
                    if (destPath.startsWith("\"") && destPath.endsWith("\"")) {
                        destPath = destPath.substring(1, destPath.length() - 1);
                    }
                    File destFile = new File(destPath);

                    try {
                        Path sourcePath = Paths.get(file.toURI());
                        Path destFilePath = Paths.get(destFile.toURI());
                        Files.copy(sourcePath, destFilePath, StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Файл успешно скопирован.");
                    } catch (IOException e) {
                        System.out.println("Ошибка при копировании файла: " + e.getMessage());
                    }
                    break;
                case 8:
                    System.out.print("Введите расширение для вывода списка файлов (вида '.txt'): ");
                    in.nextLine();
                    String fileFormat = in.nextLine();
                    if (file.isDirectory()) {
                        System.out.println("Каталог: " + file.getName() + " поиск по " + fileFormat + " {");
                        String[] dirlist = file.list();
                        for (int i = 0; i < dirlist.length; i++) {
                            File f = new File(file.getAbsolutePath() + "\\" + dirlist[i]);
                            if (f.isFile()) { // Проверяем, что это файл
                                String fileName = f.getName();
                                // Получаем расширение файла
                                String extension = "";
                                int lastIndex = fileName.lastIndexOf('.');
                                if (lastIndex >= 0) {
                                    extension = fileName.substring(lastIndex);
                                }
                                // Сравниваем расширение с введенным пользователем
                                if (extension.equals(fileFormat)) {
                                    System.out.println(" --- " + f.getName() + " - файл");
                                }
                            }
                        }
                    } else {
                        System.out.println(file.getName() + " - это не каталог");
                    }
                    System.out.println("}");
                    break;
                case 9:
                    System.out.println("Вы действительно хотите удалить этот файл или каталог?");
                    System.out.println("1. Да");
                    System.out.println("2. Нет");
                    in.nextLine();
                    System.out.print("Выберите пунк меню: ");
                    int ans = in.nextInt();

                    switch (ans) {
                        case 1:
                            deleteDir(file);
                            System.out.println();
                            break;
                        case 2:
                            continue;
                    }
                case 10:
                    System.out.print("Введите полное название файла или каталого, который требуется найти: ");
                    in.nextLine();
                    String name = in.nextLine();
                    if (searchFileorDir(name, file))
                        System.out.println("Такой файл/директория существует");
                    else
                        System.out.println("Такого файла/директории не существует");
                    break;
            }
        } while (global_ans != 11);
    }
}
