package com.backend.cineboo.utility;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.Optional;

public class RepoUtility {

    /**
     * Tìm kiếm Object theo Repo và ID
     *
     * @param id
     * @param repository
     * @return Trả về Bad Request nếu ID Null.
     * Trả về Not found nếu suatChieu không tồn tại.
     * Trả về ResponseEntity.ok(Object) nếu thành công
     * Người dùng tự cast Object để sử dụng
     */
    public static ResponseEntity findById(Long id, JpaRepository repository) {
        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không xác định được ID SuatChieu");
        }
        Object object = repository.findById(id).orElse(null);
        if (object == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("SuatChieu không tồn tại");
        }
        return ResponseEntity.ok(object);
    }

    /**
     * @param repository
     * @param columnName
     * @param value
     * @param <T>
     * @return Trả về ResponseEntity(200) nếu thành  công.
     * Trả về ResponseEntity(badRequest) nếu cột không tồn tại.
     * Trả về ResponseEntity(notFound) nếu bản ghi không tồn tại.
     * Trả về ResponseEntity(INTERNAL_SERVER_ERROR) nếu lỗi khác.
     */
    public static <T> ResponseEntity<?> findByCustomColumn(JpaRepository<T, ?> repository, String columnName, String value) {
        try {
            // Construct the method name based on the column
            String methodName = "findBy" + Character.toUpperCase(columnName.charAt(0)) + columnName.substring(1);

            // Check if the method exists in the repository
            Method method = null;
            for (Method m : repository.getClass().getMethods()) {
                if (m.getName().equals(methodName) && m.getParameterCount() == 1 && m.getParameterTypes()[0].isAssignableFrom(value.getClass())) {
                    method = m;
                    break;
                }
            }

            if (method == null) {
                // Method does not exist
                return ResponseEntity.badRequest().body("Không hỗ trợ tìm kiếm cho cột: " + columnName);
            }

            // Invoke the method if it exists
            Optional<T> result = (Optional<T>) method.invoke(repository, value);

            return result
                    .<ResponseEntity<?>>map(entity -> ResponseEntity.ok(entity)) // Return entity if present
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Không tồn tại bản ghi cần tìm")); // Return error message if not found

        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi thực hiện tìm kiếm bằng phản chiếu");
        } catch (ClassCastException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi chuyển kiểu dữ liệu");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi không xác định");
        }
    }
}