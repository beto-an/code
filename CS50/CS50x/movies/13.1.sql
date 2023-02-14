SELECT p1.name
FROM people AS p1
    JOIN stars AS s1 ON p1.id = s1.person_id
    JOIN stars AS s2 ON s1.movie_id = s2.movie_id
    JOIN people AS p2 ON s2.person_id = p2.id
WHERE p2.name IS "Kevin Bacon"
    AND p2.birth = 1958
    AND NOT (
        p1.name IS "Kevin Bacon"
        AND p1.birth = 1958
    )
GROUP BY 1;