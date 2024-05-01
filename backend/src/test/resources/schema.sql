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
	user_id varchar(20) not null,
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
)

