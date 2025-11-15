package org.bytesync.hotelmanagement.util.mapper;

import org.bytesync.hotelmanagement.dto.guest.RelationDto;
import org.bytesync.hotelmanagement.model.Relation;

public class RelationMapper {

    private RelationMapper() {
    }

    public static Relation toEntity(RelationDto dto) {
        return Relation.builder()
                .name(dto.name())
                .phoneNumber(dto.phone())
                .relation(dto.relation())
                .build();
    }

    public static RelationDto toDto(Relation entity) {
        return RelationDto.builder()
                .name(entity.getName())
                .phone(entity.getPhoneNumber())
                .relation(entity.getRelation())
                .build();
    }
}
