SELECT title FROM movies
JOIN ratings ON movies.id = ratings.movie_id
WHERE id in
(SELECT movie_id FROM stars WHERE person_ID IS
(SELECT id FROM people WHERE name IS "Chadwick Boseman"))
ORDER BY rating DESC
LIMIT 5;