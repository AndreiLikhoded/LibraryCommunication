package kz.attractor.java.lesson44;

import kz.attractor.java.libraryCommunication.Profile;
import kz.attractor.java.service.ReadersService;

import java.util.List;

public class ProfileDataModel {
    private Profile profile;

    public ProfileDataModel() {
        this.profile = new Profile(1, "Artem", "Artemiy", "qwe@qwe", "123");
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
