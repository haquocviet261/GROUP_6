package com.petshop.common;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        String folderPath = "C:\\Users\\manh3\\Storage\\domeo";


        // Sử dụng vòng lặp để thay đổi tên của từng tập tin ảnh
        for (int i = 1; i <= 35; i++) {
            // Tên cũ của tập tin
            String oldFileName = i + ".png";

            // Tên mới của tập tin
            String newFileName = "domeo_" + i + ".jpg";

            // Tạo đối tượng File cho tập tin cũ
            File oldFile = new File(folderPath, oldFileName);

            // Tạo đối tượng File cho tập tin mới
            File newFile = new File(folderPath, newFileName);

            // Thử thay đổi tên của tập tin
            boolean success = oldFile.renameTo(newFile);

            // Kiểm tra xem việc thay đổi tên tập tin đã thành công hay không
            if (success) {
                System.out.println("Đã thay đổi tên tập tin thành công từ " + oldFileName + " thành " + newFileName);
            } else {
                System.out.println("Không thể thay đổi tên tập tin từ " + oldFileName + " thành " + newFileName);
            }
        }
    }
}
