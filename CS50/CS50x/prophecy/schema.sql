DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS houses;
DROP TABLE IF EXISTS heads;
DROP TABLE IF EXISTS relations;


CREATE TABLE students (
    student_id INTEGER,
    student_name TEXT,
    PRIMARY KEY(student_id)
);

CREATE TABLE houses (
    house_id INTEGER,
    house_name TEXT,
    PRIMARY KEY(house_id)
);

CREATE TABLE heads (
    head_id INTEGER,
    head_name TEXT,
    PRIMARY KEY(head_id)
);

CREATE TABLE relations (
    student_id INTEGER,
    house_id INTEGER,
    head_id INTEGER,
    FOREIGN KEY(student_id) REFERENCES students(student_id),
    FOREIGN KEY(house_id) REFERENCES houses(house_id),
    FOREIGN KEY(head_id) REFERENCES heads(head_id)
);