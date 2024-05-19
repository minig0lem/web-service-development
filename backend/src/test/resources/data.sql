INSERT INTO question(content) values ('졸업한 초등학교 이름');
INSERT INTO question(content) values ('졸업한 중학교 이름');
INSERT INTO ticket(hour, price) values (1, 1000);
INSERT INTO ticket(hour, price) values (2, 2000);
INSERT INTO ticket(hour, price) values (24, 24000);

INSERT INTO user(user_id, password, email, phone_number, pw_question, pw_answer, role, ticket_id) values ('test', '1234', 'test', 'test', 1, 'test', 'user', 1);
INSERT INTO user(user_id, password, email, phone_number, pw_question, pw_answer, role, overfee) values ('test2', '1234', 'test', 'test', 1, 'test', 'user', 1000);
INSERT INTO user(user_id, password, email, phone_number, pw_question, pw_answer, role) values ('test3', '1234', 'test', 'test', 1, 'test', 'user');
INSERT INTO user(user_id, password, email, phone_number, pw_question, pw_answer, role, cash, overfee) values ('test4', '1234', 'test', 'test', 1, 'test', 'user', 5000, 2000);
INSERT INTO user(user_id, password, email, phone_number, pw_question, pw_answer, role) values ('admin', '1234', 'admin', 'admin', 1, 'admin', 'admin');


INSERT INTO location(location_id, address, latitude, longitude) values ('ST-10', '서울특별시 마포구 양화로 93 427', '37.552746', '126.918617');
INSERT INTO location(location_id, address, latitude, longitude) values ('ST-100', '서울특별시 광진구 아차산로 262 더샵스타시티 C동 앞', '37.536667', '127.073593');
INSERT INTO location(location_id, address, latitude, longitude) values ('ST-1000', '서울특별시 양천구 신정동 236 서부식자재마트 건너편', '37.51038', '126.866798');
INSERT INTO location(location_id, address, latitude, longitude) values ('ST-1002', '서울특별시 양천구 목동동로 316-6 서울시 도로환경관리센터', '37.5299', '126.876541');
INSERT INTO location(location_id, address, latitude, longitude) values ('ST-1003', '서울특별시 양천구 화곡로 59 신월동 이마트', '37.539551', '126.8283');

INSERT INTO bike(bike_id, location_id) values ('SPB-30063', 'ST-10');
INSERT INTO bike(bike_id, location_id) values ('SPB-30074', 'ST-10');
INSERT INTO bike(bike_id, location_id) values ('SPB-30213', 'ST-10');
INSERT INTO bike(bike_id, location_id) values ('SPB-30241', 'ST-10');
INSERT INTO bike(bike_id, location_id) values ('SPB-30259', 'ST-10');
INSERT INTO bike(bike_id, location_id) values ('SPB-30358', 'ST-100');
INSERT INTO bike(bike_id, location_id) values ('SPB-30400', 'ST-100');

INSERT INTO board(user_id, title, content) values ('test', 'test title', 'test content');

INSERT INTO board_comments(user_id, board_id, content) values ('test', 1, 'test comment');

INSERT INTO favorites(user_id, location_id) values ('test', 'ST-1002');

INSERT INTO notice(admin_id, title, content) values ('admin', 'test notice title', 'test notice content');
INSERT INTO notice(admin_id, title, content) values ('admin', 'test notice title2', 'test notice content2');

INSERT INTO rental(bike_id, user_id, start_location, end_location, rental_duration) values ('SPB-30063', 'test', 'ST-10', 'ST-10', 50);
INSERT INTO rental(bike_id, user_id, start_location, end_location, rental_duration) values ('SPB-30074', 'test', 'ST-10', 'ST-10', 50);
INSERT INTO rental(bike_id, user_id, start_location, end_location, rental_duration) values ('SPB-30213', 'test2', 'ST-10', 'ST-10', 30);
INSERT INTO rental(bike_id, user_id, start_location, end_location, rental_duration) values ('SPB-30241', 'test3', 'ST-10', 'ST-10', 20);

