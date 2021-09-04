package main;

import framework.utils.PropertyUtils;

import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        final Properties properties = PropertyUtils.readFromFile("/laboratory-framework.properties");
        properties.entrySet().forEach(System.out::println);
    }

}
