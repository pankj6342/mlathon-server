package com.mlathon.server.repositories;

import java.util.List;

import com.mlathon.server.entities.Notes;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mlathon.server.entities.User;

public interface NotesRepo extends JpaRepository<Notes, Integer>{
    List<Notes> findByUser(User user);
}
