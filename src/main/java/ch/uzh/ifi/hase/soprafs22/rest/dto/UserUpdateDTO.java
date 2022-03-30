package ch.uzh.ifi.hase.soprafs22.rest.dto;

import java.util.Date;

public class UserUpdateDTO {

    private Date birthDate;

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
