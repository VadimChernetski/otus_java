package homework;

public class CustomerUtil {

    public static Customer cloneCustomer(Customer source) {
        return source == null ? null : new Customer(source.getId(), source.getName(), source.getScores());
    }

}
