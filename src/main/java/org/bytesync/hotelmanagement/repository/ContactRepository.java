package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
