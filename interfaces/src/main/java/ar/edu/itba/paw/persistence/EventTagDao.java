package ar.edu.itba.paw.persistence;

public interface EventTagDao {
    void addTagToEvent(int eventId, int tagId);
    void cleanTagsFromEvent(int eventId);
}
