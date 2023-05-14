package ip.labwork.user.controller;

import org.springframework.data.domain.Page;

import java.util.List;

public class UsersPageDTO {
    private Page<UserDto> users;
    private List<Integer> pageNumbers;
    private int totalPages;

    public UsersPageDTO(Page<UserDto> users, List<Integer> pageNumbers, int totalPages) {
        this.users = users;
        this.pageNumbers = pageNumbers;
        this.totalPages = totalPages;
    }

    public Page<UserDto> getUsers() {
        return users;
    }

    public List<Integer> getPageNumbers() {
        return pageNumbers;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
