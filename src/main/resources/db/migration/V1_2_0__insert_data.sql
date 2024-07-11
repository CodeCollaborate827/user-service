INSERT INTO users (id, username, display_name, avatar_url, email)
VALUES
    ('1', 'nguyen_van_anh', 'Nguyễn Văn Anh', 'https://example.com/avatar1.jpg', 'nguyenvananh@example.com'),
    ('2', 'tran_thi_binh', 'Trần Thị Bình', 'https://example.com/avatar2.jpg', 'tranthibinh@example.com'),
    ('3', 'le_hoang_dieu', 'Lê Hoàng Diệu', 'https://example.com/avatar3.jpg', 'lehoangdieu@example.com'),
    ('4', 'pham_quoc_tuan', 'Phạm Quốc Tuấn', 'https://example.com/avatar4.jpg', 'phamquoctuan@example.com'),
    ('5', 'vu_thi_huong', 'Vũ Thị Hương', 'https://example.com/avatar5.jpg', 'vuthihuong@example.com'),
    ('6', 'do_hong_son', 'Đỗ Hồng Sơn', 'https://example.com/avatar6.jpg', 'dohongson@example.com'),
    ('7', 'bui_thanh_hai', 'Bùi Thanh Hải', 'https://example.com/avatar7.jpg', 'buithanhhai@example.com'),
    ('8', 'nguyen_thi_lan', 'Nguyễn Thị Lan', 'https://example.com/avatar8.jpg', 'nguyenthilan@example.com'),
    ('9', 'hoang_anh_tuan', 'Hoàng Anh Tuấn', 'https://example.com/avatar9.jpg', 'hoanganhtuan@example.com'),
    ('10', 'pham_thu_hien', 'Phạm Thu Hiền', 'https://example.com/avatar10.jpg', 'phamthuhien@example.com');

INSERT INTO user_address (user_id, country, province, city, district, ward)
VALUES
    ('1', 'Vietnam', 'Hanoi', 'Hanoi', 'Cau Giay', 'Dich Vong'),
    ('2', 'Vietnam', 'Hanoi', 'Hanoi', 'Ba Dinh', 'Truc Bach'),
    ('3', 'Vietnam', 'Ho Chi Minh', 'Ho Chi Minh City', 'District 1', 'Ben Nghe'),
    ('4', 'Vietnam', 'Ho Chi Minh', 'Ho Chi Minh City', 'District 3', 'Ward 6'),
    ('5', 'Vietnam', 'Da Nang', 'Da Nang', 'Hai Chau', 'Hai Chau 1'),
    ('6', 'Vietnam', 'Hai Phong', 'Hai Phong', 'Le Chan', 'Du Hang'),
    ('7', 'Vietnam', 'Can Tho', 'Can Tho', 'Ninh Kieu', 'An Cu'),
    ('8', 'Vietnam', 'Hue', 'Hue', 'Phu Hoi', 'Vinh Ninh'),
    ('9', 'Vietnam', 'Nha Trang', 'Nha Trang', 'Tan Lap', 'Loc Tho'),
    ('10', 'Vietnam', 'Da Lat', 'Da Lat', 'Ward 1', 'Ward 1');
