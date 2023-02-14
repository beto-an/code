SELECT name
FROM people
WHERE id IN (
        SELECT person_id
        FROM stars
        WHERE movie_id IN (
                SELECT movie_id
                FROM stars
                WHERE person_id IS (
                        SELECT id
                        FROM people
                        WHERE name IS "Kevin Bacon"
                            AND birth = 1958
                    )
            )
    )
    AND id IS NOT (
        SELECT id
        FROM people
        WHERE name IS "Kevin Bacon"
            AND birth = 1958
    )
GROUP BY 1;