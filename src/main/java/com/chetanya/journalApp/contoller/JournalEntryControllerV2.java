package com.chetanya.journalApp.contoller;

import com.chetanya.journalApp.entity.JournalEntry;
import com.chetanya.journalApp.service.JournalEntrySevice;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {
    @Autowired
    private JournalEntrySevice journalEntrySevice;

    @GetMapping
    public ResponseEntity<?> getAll() { // localhost:8080/journal GET
        List<JournalEntry> all = journalEntrySevice.getAll();
        if(all!=null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry){ // localhost:8080/journal POST
        try{
            myEntry.setDate(LocalDateTime.now());
            journalEntrySevice.saveEntry(myEntry);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId){
//        return journalEntrySevice.findById(myId).orElse(null);
        Optional<JournalEntry> journalEntry = journalEntrySevice.findById(myId);
        if(journalEntry.isPresent()){
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId){
        journalEntrySevice.deleteById(myId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateJournalEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry myEntry){
        JournalEntry old = journalEntrySevice.findById(id).orElse(null);
        if(old != null){
            old.setTitle(myEntry.getTitle()!=null && myEntry.getTitle().equals("") ? old.getTitle() : myEntry.getTitle());
            old.setContent(myEntry.getContent()!=null && myEntry.getContent().equals("") ? old.getContent() : myEntry.getContent());
            journalEntrySevice.saveEntry(old);
            return new ResponseEntity<>(old, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
