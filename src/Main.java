import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:mysql://192.168.56.101:4567/madang?useSSL=false&serverTimezone=UTC";
    private static final String USER = "kimyejin";
    private static final String PASSWORD = "1234";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("1. 삽입 | 2. 삭제 | 3. 조회 | 0. 종료");
                System.out.print("선택: ");
                int choice = scanner.nextInt();

                if (choice == 0) break;

                switch (choice) {
                    case 1 -> insertBook(conn, scanner);
                    case 2 -> deleteBook(conn, scanner);
                    case 3 -> selectBooks(conn);
                    default -> System.out.println("잘못된 입력입니다.");
                }
            }
        } catch (SQLException e) {
            System.out.println("DB 연결 실패");
        }
    }

    private static void insertBook(Connection conn, Scanner scanner) {
        System.out.print("책 ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기
        System.out.print("책 이름: ");
        String name = scanner.nextLine();
        System.out.print("출판사: ");
        String publisher = scanner.nextLine();
        System.out.print("가격: ");
        int price = scanner.nextInt();

        String sql = "INSERT INTO Book (bookid, bookname, publisher, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, publisher);
            pstmt.setInt(4, price);
            pstmt.executeUpdate();
            System.out.println("삽입 완료");
        } catch (SQLException e) {
            System.out.println("삽입 실패");
        }
    }

    private static void deleteBook(Connection conn, Scanner scanner) {
        System.out.print("삭제할 책 ID: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM Book WHERE bookid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("삭제 완료");
        } catch (SQLException e) {
            System.out.println("삭제 실패");
        }
    }

    private static void selectBooks(Connection conn) {
        String sql = "SELECT * FROM Book";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("ID: %d, 이름: %s, 출판사: %s, 가격: %d\n",
                        rs.getInt("bookid"), rs.getString("bookname"),
                        rs.getString("publisher"), rs.getInt("price"));
            }
        } catch (SQLException e) {
            System.out.println("조회 실패");
        }
    }
}
