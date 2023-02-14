WITH T1 AS (
    SELECT movie_id
    FROM stars AS s
        JOIN people AS p ON s.person_id = p.id
    WHERE p.name IS "Kevin Bacon"
)
SELECT p.name
FROM people AS p
    JOIN stars AS s ON p.id = s.person_id
    JOIN T1 ON s.movie_id = T1.movie_id
WHERE p.name IS NOT "Kevin Bacon"
    OR p.birth != 1958
GROUP BY 1;