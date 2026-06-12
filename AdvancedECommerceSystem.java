import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


class Product {
    private static final AtomicInteger ID_GEN = new AtomicInteger(100);

    private final int id;
    private final String name;
    private final double price;
    private int stock;

    public Product(String name, double price, int stock) {
        this.id = ID_GEN.incrementAndGet();
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public synchronized boolean reduceStock(int qty) {
        if (qty <= stock) {
            stock -= qty;
            return true;
        }
        return false;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    public String toString() {
        return id + " | " + name + " | ₹" + price + " | Stock: " + stock;
    }
}


class User {
    private final int userId;
    private String name;
    private double walletBalance;
    private List<Order> orderHistory = new ArrayList<>();

    public User(int userId, String name, double walletBalance) {
        this.userId = userId;
        this.name = name;
        this.walletBalance = walletBalance;
    }

    public boolean pay(double amount) {
        if (walletBalance >= amount) {
            walletBalance -= amount;
            return true;
        }
        return false;
    }

    public void addOrder(Order order) {
        orderHistory.add(order);
    }

    public void showOrders() {
        System.out.println("\nOrder History for " + name);
        for (Order o : orderHistory) {
            o.printOrder();
        }
    }

    public String getName() { return name; }
    public double getWalletBalance() { return walletBalance; }
}

class CartItem {
    Product product;
    int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public double getTotal() {
        return product.getPrice() * quantity;
    }
}


class Cart {
    private List<CartItem> items = new ArrayList<>();

    public void add(Product product, int qty) {
        items.add(new CartItem(product, qty));
    }

    public double totalAmount() {
        return items.stream().mapToDouble(CartItem::getTotal).sum();
    }

    public List<CartItem> getItems() {
        return items;
    }
}


class Order {
    private static final AtomicInteger ORDER_ID = new AtomicInteger(1000);

    private final int orderId;
    private final double amount;

    public Order(double amount) {
        this.orderId = ORDER_ID.incrementAndGet();
        this.amount = amount;
    }

    public void printOrder() {
        System.out.println("OrderID: " + orderId + " | Paid: ₹" + amount);
    }
}


class OrderService {

    public static void placeOrder(User user, Cart cart) {

        double total = cart.totalAmount();

        System.out.println("\nProcessing Order for " + user.getName());
        System.out.println("Total Amount: ₹" + total);

        // Check payment
        if (!user.pay(total)) {
            System.out.println("Payment Failed: Insufficient Balance");
            return;
        }

        // Reduce stock safely
        for (CartItem item : cart.getItems()) {
            boolean success = item.product.reduceStock(item.quantity);
            if (!success) {
                System.out.println("Stock issue for: " + item.product.getName());
                return;
            }
        }

        Order order = new Order(total);
        user.addOrder(order);

        System.out.println("Order Placed Successfully!");
        order.printOrder();
    }
}


public class AdvancedECommerceSystem {

    public static void main(String[] args) {

     
        List<Product> products = new ArrayList<>();
        products.add(new Product("Laptop", 60000, 5));
        products.add(new Product("Smartphone", 20000, 10));
        products.add(new Product("Mouse", 500, 50));

        System.out.println("=== PRODUCT LIST ===");
        products.forEach(System.out::println);

        User user1 = new User(1, "Arun", 100000);
        User user2 = new User(2, "Kiran", 15000);

        Cart cart1 = new Cart();
        cart1.add(products.get(0), 1);
        cart1.add(products.get(2), 2);

        OrderService.placeOrder(user1, cart1);

       
        Cart cart2 = new Cart();
        cart2.add(products.get(1), 1);

        OrderService.placeOrder(user2, cart2);

        user1.showOrders();
        user2.showOrders();

        System.out.println("\n=== FINAL STOCK STATUS ===");
        products.forEach(System.out::println);
    }
}
