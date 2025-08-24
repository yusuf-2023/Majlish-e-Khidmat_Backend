package com.majlishekhidmat.repositoryV2;



import com.majlishekhidmat.entityV2.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}

