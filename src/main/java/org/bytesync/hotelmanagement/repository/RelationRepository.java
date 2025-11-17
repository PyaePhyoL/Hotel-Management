package org.bytesync.hotelmanagement.repository;

import org.bytesync.hotelmanagement.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationRepository extends JpaRepository<Contact, Integer> {
}
