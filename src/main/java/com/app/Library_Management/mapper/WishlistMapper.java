package com.app.Library_Management.mapper;

import com.app.Library_Management.model.Wishlist;
import com.app.Library_Management.payload.dto.WishlistDto;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class WishlistMapper {

    private final BookMapper bookMapper;

    public WishlistMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public WishlistDto toDTO(Wishlist wishlist) {
        if (wishlist == null) {
            return null;
        }

        return WishlistDto.builder()
                .id(wishlist.getId())
                .userId(wishlist.getUser() != null ? wishlist.getUser().getId() : null)
                .userFullName(wishlist.getUser() != null ? wishlist.getUser().getFullName() : null)
                .bookDTO(wishlist.getBook() != null ? bookMapper.toDTO(wishlist.getBook()) : null)
                .addedAt(wishlist.getAddedAt())
                .notes(wishlist.getNotes())
                .build();
    }

    public Wishlist toEntity(WishlistDto dto) {
        if (dto == null) {
            return null;
        }

        return Wishlist.builder()
                .id(dto.getId())
                .notes(dto.getNotes())
                .build();
    }

    public Wishlist toEntityForCreate(WishlistDto dto) {
        if (dto == null) {
            return null;
        }

        return Wishlist.builder()
                .notes(dto.getNotes())
                .build();
    }

    public Wishlist toEntityForUpdate(WishlistDto dto) {
        if (dto == null) {
            return null;
        }

        return Wishlist.builder()
                .id(dto.getId())
                .notes(dto.getNotes())
                .build();
    }

    public List<WishlistDto> toDTOList(List<Wishlist> wishlists) {
        if (wishlists == null) {
            return List.of();
        }

        return wishlists.stream()
                .map(this::toDTO)
                .toList();
    }

    public List<Wishlist> toEntityList(List<WishlistDto> dtos) {
        if (dtos == null) {
            return List.of();
        }

        return dtos.stream()
                .map(this::toEntity)
                .toList();
    }

    public void updateEntityFromDTO(WishlistDto dto, Wishlist wishlist) {
        if (dto == null || wishlist == null) {
            return;
        }

        if (dto.getNotes() != null) {
            wishlist.setNotes(dto.getNotes());
        }
    }

}

