package com.ironmountain.imtest.endpoints;

import com.ironmountain.imtest.exceptions.messages.Msg;
import com.ironmountain.imtest.services.ContactService;
import com.ironmountain.imtest.transfer.CSVProcessedResults;
import com.ironmountain.imtest.transfer.ContactDTO;
import com.ironmountain.imtest.transfer.MessageResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/contacts")
@AllArgsConstructor
public class ContactsEndpoint {
    private final ContactService contactService;

    @GetMapping("by-id/{id}")
    public ResponseEntity<ContactDTO> contactById(@PathVariable("id") int id) {
        return ResponseEntity.ok(contactService.findById(id));
    }

    @GetMapping("paged")
    public ResponseEntity<Page<ContactDTO>> contactsPaged(@RequestParam("sort") String sort, @RequestParam("order") String order, @RequestParam("page") int page, @RequestParam("size") int pageSize, @RequestParam("filter") Optional<String> filter) {
        return ResponseEntity.ok(contactService.findPagedContacts(page, pageSize, order, sort, filter.orElse("")));
    }

    @GetMapping("by-curp")
    public ResponseEntity<Boolean> contactById(@RequestParam("curp") String curp, @RequestParam("filter") Optional<Integer> id) {
        return ResponseEntity.ok(contactService.existContact(id.orElse(null), curp));
    }

    @PostMapping("save")
    public ResponseEntity<MessageResponse<Integer>> save(@RequestBody ContactDTO contact) {
        return ResponseEntity.ok(MessageResponse.<Integer>builder()
                        .data(contactService.save(contact).getId())
                        .message(Msg.CONTACT_SAVED_OK)
                .build());
    }

    @PutMapping("update")
    public ResponseEntity<MessageResponse<Integer>> update(@RequestBody ContactDTO contact) {
        return ResponseEntity.ok(MessageResponse.<Integer>builder()
                .data(contactService.save(contact).getId())
                .message(Msg.CONTACT_UPDATED_OK)
                .build());
    }

    @DeleteMapping("by-id/{id}")
    public ResponseEntity<MessageResponse<Void>> delete(@PathVariable int id) {
        contactService.delete(id);
        return ResponseEntity.ok(MessageResponse.<Void>builder()
                .message(Msg.CONTACT_DELETED_OK)
                .build());
    }

    @PostMapping("upload")
    public ResponseEntity<MessageResponse<CSVProcessedResults>> uploadCsv(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(MessageResponse.<CSVProcessedResults>builder()
                .message(Msg.FILE_UPLOADED)
                .data(contactService.saveAllFromCSV(file.getInputStream(), true, file.getContentType()))

                .build());
    }
 }
