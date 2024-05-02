CREATE TABLE question(
	question_id int not null auto_increment,
    content text not null,
    PRIMARY KEY(question_id)
);

CREATE TABLE ticket(
	ticket_id int not null auto_increment,
    hour varchar(10) not null,
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
    CONSTRAINT user_question FOREIGN KEY(pw_question) REFERENCES question(question_id)
    ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT user_ticket FOREIGN KEY(ticket_id) REFERENCES ticket(ticket_id)
    ON DELETE NO ACTION ON UPDATE NO ACTION
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
    ON DELETE NO ACTION ON UPDATE NO ACTION
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
    ON DELETE NO ACTION ON UPDATE NO ACTION
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
    ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE board_views(
    view_id int not null auto_increment,
    user_id varchar(50) not null,
    board_id int not null,
    PRIMARY KEY(view_id),
    CONSTRAINT board_views_user FOREIGN KEY(user_id) REFERENCES user(user_id),
    CONSTRAINT board_views_board FOREIGN KEY(board_id) REFERENCES board(board_id)
    ON DELETE NO ACTION ON UPDATE NO ACTION
);


