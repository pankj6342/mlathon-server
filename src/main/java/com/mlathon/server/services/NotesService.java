package com.mlathon.server.services;

import java.util.List;

import com.mlathon.server.payload.NotesDto;
import org.springframework.stereotype.Service;

@Service
public interface NotesService {
	
	//create
	NotesDto createNote(NotesDto notesDto, String userId);
	
	
	//update
	NotesDto updateNote(NotesDto notesDto,Integer notesId);
	
	//delete
	void deleteNote(Integer notesId);
	
	//get
	NotesDto getNote(Integer notesId);
	
	//getAll
	List<NotesDto> getAllNote();

	//getByUser
    List<NotesDto> getNoteByUser(String userId);
}
