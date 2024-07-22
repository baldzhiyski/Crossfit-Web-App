package com.softuni.crossfitapp.service.impl;

import com.softuni.crossfitapp.domain.entity.Membership;
import com.softuni.crossfitapp.domain.entity.Role;
import com.softuni.crossfitapp.domain.entity.User;
import com.softuni.crossfitapp.domain.entity.enums.MembershipType;
import com.softuni.crossfitapp.domain.entity.enums.RoleType;
import com.softuni.crossfitapp.repository.MembershipRepository;
import com.softuni.crossfitapp.repository.RoleRepository;
import com.softuni.crossfitapp.repository.UserRepository;
import com.softuni.crossfitapp.testUtils.TestData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SeedServiceImplTest {

    @Autowired
    private SeedServiceImpl seedService;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestData testData;

    @AfterEach
    public void tearDown(){
        this.userRepository.deleteAll();
        this.roleRepository.deleteAll();
        this.membershipRepository.deleteAll();
    }


    @Test
    void testSeedRoles() throws FileNotFoundException {
        // Ensure roles are not present initially
        assertThat(roleRepository.count()).isEqualTo(0);

        // Call the seed method
        seedService.seedRoles();

        // Verify roles are seeded correctly
        assertThat(roleRepository.count()).isGreaterThan(0);

        // Example: Assert a specific role is present
        Role coachRole = roleRepository.findByRoleType(RoleType.COACH);
        assertThat(coachRole).isNotNull();
    }

    @Test
    void testSeedMemberships() throws FileNotFoundException {
        // Ensure memberships are not present initially
        assertThat(membershipRepository.count()).isEqualTo(0);

        // Call the seed method
        seedService.seedMemberships();

        // Verify memberships are seeded correctly
        assertThat(membershipRepository.count()).isGreaterThan(0);

        // Example: Assert a specific membership is present
        Membership membership = membershipRepository.findByMembershipType(MembershipType.ELITE).orElseThrow();
        assertThat(membership).isNotNull();
        assertThat(membership.getPrice()).isEqualTo(130L); // Adjust based on your data

        Membership membership2 = membershipRepository.findByMembershipType(MembershipType.PREMIUM).orElseThrow();
        assertThat(membership2).isNotNull();
        assertThat(membership2.getPrice()).isEqualTo(100L); // Adjust based on your data

        Membership membership3 = membershipRepository.findByMembershipType(MembershipType.UNLIMITED).orElseThrow();
        assertThat(membership3).isNotNull();
        assertThat(membership3.getPrice()).isEqualTo(400L); // Adjust based on your data
    }


    @Test
    void testSeedCoachesUserAccount() throws FileNotFoundException {


        // Ensure users are not present initially
        assertThat(userRepository.count()).isEqualTo(0);

        seedCountries(testData);

        seedService.seedRoles();
        seedService.seedMemberships();
        // Call the seed method
        seedService.seedCoachesUserAccount();

        // Verify users are seeded correctly
        assertThat(userRepository.count()).isGreaterThan(0);

        // Example: Assert a specific user/coach is present
        List<User> coaches = userRepository.findAllByRolesContaining(roleRepository.findByRoleType(RoleType.COACH));
        assertThat(coaches).isNotNull();
        assertTrue(coaches.size()>0);

        // Example: Assert membership and country are set correctly
        User coach = coaches.get(0); // Assuming there's at least one coach seeded
        assertThat(coach.getCountry()).isNotNull();
        assertThat(coach.getMembership()).isNotNull();
    }



    private void seedCountries(TestData testData) {
        testData.createCountry("DE","Deutschland");
        testData.createCountry("BG","Bulgaria");
    }




}