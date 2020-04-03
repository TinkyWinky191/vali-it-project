package ee.valitit.project.dto;

import ee.valitit.project.domain.*;

public final class DomainDTOFabric {

    public static UserDTO create(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profilePictureUrl(user.getProfilePictureUrl())
                .email(user.getEmail())
                .build();
    }

    public static CategoryDTO create(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public static ThemeDTO create(Theme theme) {
        return ThemeDTO.builder()
                .id(theme.getId())
                .name(theme.getName())
                .description(theme.getDescription())
                .build();
    }

    public static NoteDTO create(Note note) {
        return NoteDTO.builder()
                .id(note.getId())
                .name(note.getName())
                .contentText(note.getContentText())
                .build();
    }

}
