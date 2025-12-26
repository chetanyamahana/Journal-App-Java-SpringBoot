package com.chetanya.journalApp.contoller;

import com.chetanya.journalApp.entity.JournalEntry;
import com.chetanya.journalApp.service.JournalEntrySevice;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {
    @Autowired
    private JournalEntrySevice journalEntrySevice;

    @GetMapping
    public List<JournalEntry> getAll() { // localhost:8080/journal GET
        return journalEntrySevice.getAll();
    }

    @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry myEntry){ // localhost:8080/journal POST
        myEntry.setDate(LocalDateTime.now());
        journalEntrySevice.saveEntry(myEntry);
        return myEntry;
    }

    @GetMapping("id/{myId}")
    public JournalEntry getJournalEntryById(@PathVariable ObjectId myId){
        return journalEntrySevice.findById(myId).orElse(null);
    }

    @DeleteMapping("id/{myId}")
    public boolean deleteJournalEntryById(@PathVariable ObjectId myId){
        journalEntrySevice.deleteById(myId);
        return true;
    }

    @PutMapping("/id/{id}")
    public JournalEntry deleteJournalEntryById(@PathVariable ObjectId id, @RequestBody JournalEntry myEntry){
        JournalEntry old = journalEntrySevice.findById(id).orElse(null);
        if(old != null){
            old.setTitle(myEntry.getTitle()!=null && myEntry.getTitle().equals("") ? old.getTitle() : myEntry.getTitle());
            old.setContent(myEntry.getContent()!=null && myEntry.getContent().equals("") ? old.getContent() : myEntry.getContent());
        }
        journalEntrySevice.saveEntry(old);
        return old;
    }
}
