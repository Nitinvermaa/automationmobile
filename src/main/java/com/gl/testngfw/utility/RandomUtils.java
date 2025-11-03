package com.gl.testngfw.utility;

import java.util.*;

/**
 * Helper class to share general utility methods for generation of random numbers, date etc.
 */
public class RandomUtils {

    /**
     * This method generates random character depending on modes(Alphabetic,Alphanumeric,numeric) and length
     *
     * @param length the length of string
     * @param mode   different types of mode
     * @return String of characters
     */
    public static String generateRandomString(int length, Mode mode) {

        StringBuilder buffer = new StringBuilder();
        String characters = mode.getStringValue();
        int charactersLength = characters.length();

        for (int i = 0; i < length; i++) {
            double index = Math.random() * charactersLength;
            buffer.append(characters.charAt((int) index));
        }
        return buffer.toString();
    }

    /**
     * This method generates random date depending on year range
     *
     * @param startYearRange the starting year
     * @param endYearRange   the ending year
     * @return String containing random date.
     */
    public static String getRandomDateInRange(int startYearRange, int endYearRange) {
        GregorianCalendar gc = new GregorianCalendar();
        int year = randomBetween(startYearRange, endYearRange);
        gc.set(Calendar.YEAR, year);
        int dayOfYear = randomBetween(1, gc.getActualMaximum(Calendar.DAY_OF_YEAR));
        gc.set(Calendar.DAY_OF_YEAR, dayOfYear);
        return gc.get(Calendar.YEAR) + "-" + gc.get(Calendar.MONTH) + "-" + gc.get(Calendar.DAY_OF_MONTH);

    }

    /**
     * Returns random element of list
     *
     * @param list List to process
     * @return Random element
     */
    public static <T> T getRandomFromList(List<T> list) {
        Integer rand = randomBetween(0, list.size() - 1);
        return list.get(rand);
    }

    /**
     * Generates random number
     *
     * @param start input number
     * @param end   ending number
     * @return random number
     */
    public static int randomBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    /**
     * Generates random numbers
     * param number number of unique values to generate
     *
     * @param start input number
     * @param end   ending number
     * @return random numbers
     */
    public static List<Integer> randomNumbersBetween(int start, int end, int number) {
        HashSet<Integer> setOfUniqueNumbers = new HashSet<>();
        Integer randomNumber;
        while (setOfUniqueNumbers.size() != number) {
            randomNumber = RandomUtils.randomBetween(start, end);
            setOfUniqueNumbers.add(randomNumber);
        }

        return new ArrayList<>(setOfUniqueNumbers);
    }

    /**
     * Generates random Long number in specified range
     *
     * @param start Start range
     * @param end   End range
     * @return Long number
     */
    public static Long randomBetween(Long start, Long end) {
        return start + Math.round(Math.random() * (end - start));
    }

    public static Double randomBetween(Double start, Double end) {
        return start + Math.random() * (end - start);
    }

    /**
     * Generate random strings of required length
     *
     * @param length    - Length of generated string
     * @param inputText - Text that use for generating
     * @return Generated string
     */
    public static List getRandomString(int length, String inputText) {
        int len = length;
        int textLength = inputText.length();
        int i = 0;
        ArrayList<String> subStringList = new ArrayList<>();
        while (i < inputText.length()) {
            if (textLength > len) {
                subStringList.add(inputText.substring(i, i + len));
                textLength = textLength - len;
            } else {
                len = textLength;
                subStringList.add(inputText.substring(i, i + len));
            }
            i = i + len;
        }

        return subStringList;
    }

    /**
     * Gets random list from array of specified length
     *
     * @param array  Data to process
     * @param length List length
     * @return List of values
     * @throws Exception Incorrect Length value.
     */
    public static <T> List<T> getRandomSubList(T[] array, Integer length) {
        if (array.length < length) {
            throw new IllegalArgumentException("Incorrect Length value.");
        }
        ArrayList<T> longList = new ArrayList<>(Arrays.asList(array));
        List<T> result = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            int index = randomBetween(0, longList.size() - 1);
            result.add(longList.get(index));
            longList.remove(index);
        }
        return result;
    }

    /**
     * Gets random list from array of specified length
     *
     * @param list   Data to process
     * @param length List length
     * @return List of values
     * @throws Exception Incorrect Length value
     */
    public static <T> List<T> getRandomList(List<T> list, Integer length) {
        if (list.size() < length) {
            throw new IllegalArgumentException("Incorrect Length value.");
        }
        List<T> listToWorkWith = new ArrayList<>(list.size());
        listToWorkWith.addAll(list);

        List<T> result = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            int index = randomBetween(0, listToWorkWith.size() - 1);
            result.add(listToWorkWith.get(index));
            listToWorkWith.remove(index);
        }
        return result;
    }

    /**
     * Gets sequence as list for Long values between start and finish (both of them are include in the List) with step 1
     *
     * @param start  - value for the first element in the list
     * @param finish - value for the the last element in the list
     * @return Generated
     */
    public static List<Integer> generateSequenceIntList(Integer start, Integer finish) {
        List<Integer> list = new ArrayList<>();
        for (Integer i = start; i <= finish; i++) {
            list.add(i);
        }
        return list;
    }

    public enum Mode {

        NUMERIC("1234567890"),
        SYMBOLIC("'-=[];,./~!@#$%^&*()_+{}:\" <>?"),
        SPECIAL(SYMBOLIC.getStringValue() + "\\"),

        /* LATIN */
        ALPHA("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"),
        ALPHANUMERIC(ALPHA.getStringValue() + NUMERIC.getStringValue()),
        ALPHANUMERICSPECIALCHAR(ALPHANUMERIC.getStringValue() + SPECIAL.getStringValue()),

        ALPHANUMERICSYMBOLIC(ALPHANUMERIC.getStringValue() + SYMBOLIC.getStringValue());

        private final String stringValue;

        Mode(String s) {
            stringValue = s;
        }

        public String getStringValue() {
            return stringValue;
        }
    }
}