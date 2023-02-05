

SELECT l.locationId, l.name, COUNT(e.eventid)
FROM locations l JOIN events e ON l.locationId = e.locationId
GROUP BY l.locationId
ORDER BY l.name


SELECT t.tagId, t.name, COUNT(e.eventid)
FROM tags t JOIN eventtags et ON t.tagId = et.tagId JOIN events e ON et.eventid = e.eventId
GROUP BY t.tagId
ORDER BY t.name