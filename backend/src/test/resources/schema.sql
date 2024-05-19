CREATE TABLE question(
	question_id int not null auto_increment,
    content text not null,
    PRIMARY KEY(question_id)
);

CREATE TABLE ticket(
	ticket_id int not null auto_increment,
    hour varchar(10) not null,
    price int not null,
    PRIMARY KEY(ticket_id)
);

CREATE TABLE user(
	user_id varchar(50) not null,
    password varchar(70) not null,
    email varchar(50) not null,
    phone_number varchar(20) not null,
    pw_question int not null,
    pw_answer varchar(50) not null,
    role varchar(10) not null,
    cash int unsigned not null default 0,
    overfee int unsigned not null default 0,
    ticket_id int default null,
    PRIMARY KEY (user_id),
    CONSTRAINT user_question FOREIGN KEY(pw_question) REFERENCES question(question_id),
    CONSTRAINT user_ticket FOREIGN KEY(ticket_id) REFERENCES ticket(ticket_id)
    ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE refreshtoken(
    user_id varchar(45) not null,
    refreshToken varchar(500) not null,
    PRIMARY KEY (user_id)
);

CREATE TABLE location(
	location_id varchar(20) not null,
    address varchar(200) not null,
    latitude varchar(30) not null,
    longitude varchar(30) not null,
    status varchar(20) not null default 'available',
    PRIMARY KEY(location_id, status)
);

CREATE TABLE bike(
	bike_id varchar(10) not null,
    location_id varchar(10) not null,
    status varchar(20) not null default 'available',
    PRIMARY KEY(bike_id),
    CONSTRAINT bike_location FOREIGN KEY(location_id) REFERENCES location(location_id)
    ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE board(
	board_id int not null auto_increment,
    user_id varchar(50) not null,
    title varchar(40) not null,
    content text not null,
    created_at timestamp not null default CURRENT_TIMESTAMP,
    updated_at timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    views int not null default 0,
    PRIMARY KEY(board_id),
    CONSTRAINT board_user FOREIGN KEY(user_id) REFERENCES user(user_id)
    ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE board_comments(
    comment_id int not null auto_increment,
    user_id varchar(50) not null,
    board_id int not null,
    created_at timestamp not null default CURRENT_TIMESTAMP,
    updated_at timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    content text not null,
    PRIMARY KEY(comment_id),
    CONSTRAINT board_comments_user FOREIGN KEY(user_id) REFERENCES user(user_id),
    CONSTRAINT board_comments_board FOREIGN KEY(board_id) REFERENCES board(board_id)
    ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE board_views(
    view_id int not null auto_increment,
    user_id varchar(50) not null,
    board_id int not null,
    PRIMARY KEY(view_id),
    CONSTRAINT board_views_user FOREIGN KEY(user_id) REFERENCES user(user_id),
    CONSTRAINT board_views_board FOREIGN KEY(board_id) REFERENCES board(board_id)
    ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE favorites(
    user_id varchar(50) not null,
    location_id varchar(20) not null,
    PRIMARY KEY(user_id, location_id),
    CONSTRAINT favorites_user FOREIGN KEY(user_id) REFERENCES user(user_id),
    CONSTRAINT favorites_location FOREIGN KEY(location_id) REFERENCES location(location_id)
    ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE notice(
    notice_id int not null auto_increment,
    admin_id varchar(50) not null,
    title varchar(40) not null,
    content text not null,
    created_at timestamp not null default CURRENT_TIMESTAMP,
    updated_at timestamp not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    views int not null default 0,
    PRIMARY KEY(notice_id),
    CONSTRAINT notice_admin FOREIGN KEY(admin_id) REFERENCES user(user_id)
    ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE notice_views(
    view_id int not null auto_increment,
    admin_id varchar(50) not null,
    notice_id int not null,
    PRIMARY KEY(view_id),
    CONSTRAINT notice_views_admin FOREIGN KEY(admin_id) REFERENCES user(user_id),
    CONSTRAINT notice_views_notice FOREIGN KEY(notice_id) REFERENCES notice(notice_id)
    ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE rental(
    bike_id varchar(10) not null,
    start_time timestamp not null default CURRENT_TIMESTAMP,
    end_time timestamp default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id varchar(50) not null,
    start_location varchar(20) not null,
    end_location varchar(20) default null,
    rental_duration int not null default 0,
    fee int not null default 0,
    PRIMARY KEY(bike_id, start_time),
    CONSTRAINT rental_bike FOREIGN KEY(bike_id) REFERENCES bike(bike_id),
    CONSTRAINT rental_user FOREIGN KEY(user_id) REFERENCES user(user_id),
    CONSTRAINT rental_s_location FOREIGN KEY(start_location) REFERENCES location(location_id),
    CONSTRAINT rental_e_location FOREIGN KEY(end_location) REFERENCES location(location_id)
    ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE report(
    report_id int not null auto_increment,
    user_id varchar(50) not null,
    bike_id varchar(10) not null,
    created_at timestamp not null default CURRENT_TIMESTAMP,
    tire TINYINT not null,
    chain TINYINT not null,
    saddle TINYINT not null,
    pedal TINYINT not null,
    terminal TINYINT not null,
    status varchar(20) not null default 'received',
    PRIMARY KEY(report_id),
    CONSTRAINT report_user FOREIGN KEY(user_id) REFERENCES user(user_id),
    CONSTRAINT report_bike FOREIGN KEY(bike_id) REFERENCES bike(bike_id)
    ON DELETE RESTRICT ON UPDATE RESTRICT
);




