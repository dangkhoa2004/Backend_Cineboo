import org.mindrot.jbcrypt.BCrypt;

public class createHashedPassword {
    //Tạo mật khẩu đã mã hoá thay vì sử dụng hàm add
    //TenDangNhap và MatKhau(mã hoá) thêm thủ công vào cơ sở dữ liệu
    public static void main(String[] args) {
        String pw2 = "mk2";
        String pw3 = "mk3";
        String pw4 = "mk4";

        String hashedPassword1 = BCrypt.hashpw(pw2, BCrypt.gensalt());
        String hashedPassword2= BCrypt.hashpw(pw3, BCrypt.gensalt());
        String hashedPassword3 = BCrypt.hashpw(pw4, BCrypt.gensalt());
        System.out.println("Hashed password: " + hashedPassword1);
        System.out.println("Hashed password: " + hashedPassword2);
        System.out.println("Hashed password: " + hashedPassword3);
    }
}
