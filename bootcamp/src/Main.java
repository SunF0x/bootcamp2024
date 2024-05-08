import java.util.Random;

public class Main {
    public static String getRandomIntegerBetweenRange(int min, int max){
        Random rn = new Random();
        String x = "";
        x += rn.nextInt((max - min + 1)) + min;
        return x;
    }

    public static void main(String[] args) {
        String msisdn = "8" + getRandomIntegerBetweenRange(10000, 99999) + getRandomIntegerBetweenRange(10000, 99999);

        System.out.printf(msisdn);
    }
}