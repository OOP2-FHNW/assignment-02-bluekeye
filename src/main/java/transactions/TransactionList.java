package transactions;

import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

/**
 * @author Dieter Holz
 */
public class TransactionList {
    private final List<Transaction> allTransactions = new ArrayList<>();

    //default constructor

    public void addTransaction(Transaction transaction) {
        allTransactions.add(transaction);
    }

    public int size() {
        return allTransactions.size();
    }

    public List<Trader> allTraders() {
        List<Trader> allTraders = allTransactions.stream()
                .map(Transaction::getTrader)
                .collect(toList());
        return allTraders;
    }

    public List<Transaction> transactionsInYear(int year) {
        // sorted by year and value
        List<Transaction> years = allTransactions.stream()
                .filter(y -> y.getYear() == year)
                .sorted(comparing(Transaction::getValue))
                .collect(toList());
        return years;
    }

    public List<String> cities() {
        List<String> cities = allTraders().stream()
                .map(Trader::getCity)
                .distinct()
                .collect(toList());
        return cities;
    }

    /**
     * @param city the trader's city
     * @return all traders from given city sorted by name.
     */
    public List<Trader> traders(String city) {
        // if unknown city: don't create list
        List<Trader> tradersOfCity = allTraders().stream()
                .filter(c -> c.getCity() == city)
                .sorted(comparing(Trader::getName))
                .distinct()
                .collect(toList());
        return tradersOfCity;
    }

    /**
     * Returns a Map of all transactions.
     *
     * @return a Map with the year as key and a list of all transaction of this year as value
     */
    public Map<Integer, List<Transaction>> transactionsByYear() {
        List<Transaction> allTransactionsByYear = allTransactions.stream()
                .sorted(comparing(Transaction::getYear))
                .collect(toList());
        Map<Integer, List<Transaction>> map = allTransactionsByYear.stream()
                .collect(groupingBy(Transaction::getYear)); // value::key
        return map;
    }

    /**
     * @param city the city
     * @return true if there are any trader based in given city
     */
    public boolean traderInCity(String city) {
        List<String> cities = cities();
        boolean cityPresent = cities.stream().anyMatch(c -> c == city);
        return cityPresent;
    }

    /**
     * @param from the trader's current location
     * @param to   the trader's new location
     */
    public void relocateTraders(String from, String to) {
        // all traders from one city to the other city
        List<Trader> tradersOfCity = traders(from);
        tradersOfCity.stream().forEach(t -> t.setCity(to));
    }

    /**
     * @return the highest value in all the transactions
     */
    public int highestValue() {
        Integer highestValue = allTransactions.stream()
                .mapToInt(Transaction::getValue)
                .max()
                .orElseThrow(NoSuchElementException::new);
        return highestValue;
    }

    /**
     * @return the sum of all transaction values
     */
    public int totalValue() {
        Integer totalValue = allTransactions.stream()
                .collect(summingInt(Transaction::getValue));
        return totalValue;
    }

    /**
     * @return the transactions.Transaction with the lowest value
     */
    public Transaction getLowestValueTransaction(){
        Transaction lowestValueT = allTransactions.stream()
                .min(comparing(Transaction::getValue))
                .orElseThrow(NoSuchElementException::new);
        return lowestValueT;
    }

    /**
     * @return a string of all traders’ names sorted alphabetically
     */
    public String traderNames() {
        String traderNames= allTraders().stream()
                .map(Trader::getName)
                .distinct()
                .sorted()
                .collect(joining());
        return traderNames;
    }

}
