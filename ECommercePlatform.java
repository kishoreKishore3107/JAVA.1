import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

class Product {
    private static final AtomicInteger ID_GEN = new AtomicInteger(0);

    private final int id;
    private String name;
    private double price;
    private int stock;

    public Product(String name, double price, int stock) {
        this.id = ID_GEN.incrementAndGet();
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    public void reduceStock(int qty) {
        if (qty <= stock) {
            stock -= qty;
        }
    }

    public String toString() {
        return id + " | " + name + " | ₹" + price + " | Stock: " + stock;
    }
}

class CartItem {
    Product product;
    int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}

class Cart {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(Product product, int quantity) {
        if (product.getStock() >= quantity) {
            items.add(new CartItem(product, quantity));
            product.reduceStock(quantity);
        } else {
            System.out.println("Not enough stock for " + product.getName());
        }
    }

    public double calculateTotal() {
        return items.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }

    public void showCart() {
        System.out.println("\n--- Cart Details ---");
        for (CartItem item : items) {
            System.out.println(item.product.getName() + " x " + item.quantity +
                    " = ₹" + item.getTotalPrice());
        }
        System.out.println("Total: ₹" + calculateTotal());
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
        System.out.println("\nOrder Placed Successfully!");
        System.out.println("Order ID: " + orderId);
        System.out.println("Total Paid: ₹" + amount);
    }
}


public class ECommercePlatform {

    public static void main(String[] args) {


        List<Product> products = new ArrayList<>();
        products.add(new Product("Laptop", 55000, 5));
        products.add(new Product("Phone", 20000, 10));
        products.add(new Product("Headphones", 1500, 20));

        System.out.println("=== Product Catalog ===");
        products.forEach(System.out::println);


        Cart cart = new Cart();
        cart.addItem(products.get(0), 1);
        cart.addItem(products.get(1), 2);

        cart.showCart();

        double total = cart.calculateTotal();
        double discount = total > 50000 ? total * 0.10 : 0;

        double finalAmount = total - discount;

        System.out.println("\nDiscount Applied: ₹" + discount);
        System.out.println("Final Amount: ₹" + finalAmount);

        Order order = new Order(finalAmount);
        order.printOrder();
    }
}
