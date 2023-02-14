from cs50 import SQL
import csv

def main():
    db = SQL("sqlite:///new_students.db")

    students = []
    houses = {}
    house_id = 1
    heads = {}
    head_id = 1
    relations = []

    with open("students.csv") as file:
        reader = csv.DictReader(file)
        for row in reader:
            students.append({row["student_name"] : int(row["id"])})
            if (row["house"] not in houses):
                houses[row["house"]] = house_id
                house_id += 1
            if (row["head"] not in heads):
                heads[row["head"]] = head_id
                head_id += 1
            relations.append({
                "student_id": int(row["id"]),
                "house_id": houses[row["house"]],
                "head_id": heads[row["head"]]
            })

    for student in students:
        for name in student:
            db.execute("INSERT INTO students (student_id, student_name) VALUES(?, ?)", student[name], name)

    for house in houses:
        db.execute("INSERT INTO houses (house_id, house_name) VALUES(?, ?)", houses[house], house)

    for head in heads:
        db.execute("INSERT INTO heads (head_id, head_name) VALUES(?, ?)", heads[head], head)

    for relation in relations:
        db.execute("INSERT INTO relations (student_id, house_id, head_id) VALUES(?, ?, ?)", relation["student_id"], relation["house_id"], relation["head_id"])


if __name__ == "__main__":
    main()