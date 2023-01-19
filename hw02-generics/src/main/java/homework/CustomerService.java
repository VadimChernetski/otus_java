package homework;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    private final NavigableMap<Customer, String> customerStringMap =
            new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    public Map.Entry<Customer, String> getSmallest() {
        return cloneEntry(customerStringMap.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return cloneEntry(customerStringMap.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        customerStringMap.put(customer, data);
    }

    private Map.Entry<Customer, String> cloneEntry(Map.Entry<Customer, String> entry) {
        return entry == null ?
                null : Map.entry(CustomerUtil.cloneCustomer(entry.getKey()), entry.getValue());
    }

}
