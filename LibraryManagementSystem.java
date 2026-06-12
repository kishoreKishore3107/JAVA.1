import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

class Book {
    private static final AtomicInteger ID_GEN = new AtomicInteger(500);

    private final int bookId;
    private String title;
    private String author;
    private boolean isIssued = false;

    public Book(String title, String author) {
        this.bookId = ID_GEN.incrementAndGet();
        this.title = title;
        this.author = author;
    }

    public boolean issueBook() {
        if (!isIssued) {
            isIssued = true;
            return true;
        }
        return false;
    }

    public void returnBook() {
        isIssued = false;
    }

    public boolean isAvailable() {
        return !isIssued;
    }

    public int getBookId() { return bookId; }

    public String toString() {
        return bookId + " | " + title + " | " + author + " | " +
                (isIssued ? "Issued" : "Available");
    }
}


class Member {
    private static final AtomicInteger ID_GEN = new AtomicInteger(1000);

    private final int memberId;
    private String name;
    private List<String> history = new ArrayList<>();

    public Member(String name) {
        this.memberId = ID_GEN.incrementAndGet();
        this.name = name;
    }

    public void addHistory(String record) {
        history.add(record);
    }

    public void showHistory() {
        System.out.println("\nHistory for " + name);
        for (String h : history) {
            System.out.println(h);
        }
    }

    public int getMemberId() { return memberId; }
}


class Library {
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public void showBooks() {
        System.out.println("\n=== BOOK LIST ===");
        for (Book b : books) {
            System.out.println(b);
        }
    }

    public Book findBook(int id) {
        for (Book b : books) {
            if (b.getBookId() == id) return b;
        }
        return null;
    }

    public void issueBook(Member m, int bookId) {
        Book b = findBook(bookId);
        if (b != null && b.issueBook()) {
            m.addHistory("Issued book: " + b.getBookId());
            System.out.println("Book issued successfully!");
        } else {
            System.out.println("Book not available!");
        }
    }

    public void returnBook(Member m, int bookId, int daysLate) {
        Book b = findBook(bookId);
        if (b != null) {
            b.returnBook();

            int fine = daysLate * 10; // ₹10 per day late
            m.addHistory("Returned book: " + bookId + " | Fine: ₹" + fine);

            System.out.println("Book returned. Fine = ₹" + fine);
        }
    }
}

public class LibraryManagementSystem {

    public static void main(String[] args) {

        Library library = new Library();

   
        library.addBook(new Book("Java Programming", "James Gosling"));
        library.addBook(new Book("Data Structures", "Robert Lafore"));
        library.addBook(new Book("Operating Systems", "Galvin"));

        Member m1 = new Member("Arun");
        Member m2 = new Member("Kiran");

        library.showBooks();


        library.issueBook(m1, 501);
        library.issueBook(m2, 502);

        library.showBooks();


        library.returnBook(m1, 501, 3);

        library.showBooks();

        m1.showHistory();
        m2.showHistory();
    }
}
