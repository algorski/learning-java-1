import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    static Map<Integer, Long> rates = new HashMap<Integer, Long>();

    static String[] menu = {
        "Выйти",
        "Купить валюту",
        "Продать валюту",
        "Показать курсы валют",
    };

    static String[] currencies = {
        "Вернуться в меню",
        "USD",
        "EUR",
        "GBP",
        "CNY",
    };

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("КОНВЕРТЕР ВАЛЮТ\n");
        boolean run = true;
        while(run) {
            run = process();
        }

        scanner.close();
    }

    static boolean process() {
        byte operationId = read("Выберите операцию:", menu);
        switch (operationId) {
            case 0:
                System.out.println("До свидания!");
                return false;
            case 1:
                buy();
                break;
            case 2:
                sell();
                break;
            case 3:
                showRates();
                break;
            case 99:
                setRates();
                break;
            default:
                System.out.println("Некорректный код выбора. Попробуйте снова.");
                break;
        }
        return true;
    }

    static void buy() {
        deal(
            "Я хочу купить",
            "Для приобретения %d %s вам потребуется %.2f рублей.\n",
            "На %d рублей вы можете приобрести %.2f %s.\n"
        );
    }

    static void sell() {
        deal(
            "Я хочу продать",
            "От продажи %d %s вы выручите %.2f рублей.\n",
            "Для получения %d рублей нужно продать %.2f %s.\n"
        );
    }

    private static void deal(String action, String toRublesResultMessage, String toCurrencyResultMessage) {
        int currencyId = read(action, currencies);
        switch (currencyId) {
            case 0:
                return;
            case 1:
            case 2:
            case 3:
            case 4:
                if (null == rates.get(currencyId)) {
                    System.out.println("Для " + currencies[currencyId] + " не установлен курс.");
                    return;
                }
                break;
            default:
                System.out.println("Некорректный код выбора. Попробуйте снова.");
                return;
        }

        int amount;
        String[] variants = {"Выход", "конкретную сумму валюты", "валюты на конкретную сумму"};
        byte variant = read(action, variants);
        switch (variant) {
            case 0:
                return;
            case 1:
                System.out.print("Введите сумму в " + currencies[currencyId] + ": ");
                amount = scanner.nextInt();
                System.out.printf(toRublesResultMessage, amount, currencies[currencyId], currencyToRubles(amount, currencyId));
                break;
            case 2:
                System.out.print("Введите сумму в рублях: ");
                amount = scanner.nextInt();
                System.out.printf(toCurrencyResultMessage, amount, rublesToCurrency(amount, currencyId), currencies[currencyId]);
                break;
            default:
                System.out.println("Некорректный код выбора. Попробуйте снова.");
        }
    }

    private static void showRates() {
        for (int i = 1; i < currencies.length; i++) {
            if (rates.get(i) != null) {
                System.out.println(currencies[i] + ": " + (rates.get(i) / 100.0));
            } else {
                System.out.println("Курс для " + currencies[i] + " не установлен");
            }
        }

        System.out.println("\n\n");
    }

    private static void setRates() {
        System.out.println("Maintenance mode");
        System.out.println("Установка курсов валют");
        int currencyId = read("Выберите валюту для редактирования", currencies);
        if (currencyId > 0 && currencyId < currencies.length) {
            System.out.println("Редактируем курс " + currencies[currencyId]);
            if (rates.get(currencyId) != null) {
                System.out.println("Текущий курс: " + (rates.get(currencyId) / 100.0));
            }
            System.out.print("Введите новый курс (0 для выхода): ");
            long rate = Math.round(scanner.nextDouble() * 100);
            if (rate > 0) {
                rates.put(currencyId, rate);
                System.out.println("Текущий курс: " + (rates.get(currencyId) / 100.0));
            }
            setRates();
        } else {
            System.out.println("Выход из режима обслуживания");
        }
    }

    private static double rublesToCurrency(int amount, int currencyId) {
        return amount / (rates.get(currencyId) / 100.0);
    }

    private static double currencyToRubles(int amount, int currencyId) {
        return amount * rates.get(currencyId) / 100.0;
    }

    private static byte read(String message, String[] options) {
        System.out.println(message);
        System.out.println("------------------\n");
        for (int i = 1; i < options.length; i++) {
            if (!options[i].isEmpty()) {
                System.out.println(i + ". " + options[i]);
            }
        }
        System.out.println("\n0. " + options[0] + "\n");
        System.out.println("------------------");
        System.out.print("Выбор: ");
        return scanner.nextByte();
    }
}
