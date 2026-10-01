package com.pietos.bgv.config;

import com.pietos.bgv.constant.RoleNames;
import com.pietos.bgv.entity.Role;
import com.pietos.bgv.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleDataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleDataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {

        List<RoleData> roles = List.of(
                new RoleData(
                        RoleNames.SUPER_ADMIN,
                        "Full system access"
                ),
                new RoleData(
                        RoleNames.ADMIN,
                        "Administrative access"
                ),
                new RoleData(
                        RoleNames.DATA_ENTRY_TEAM_MEMBER,
                        "Handles data entry operations"
                ),
                new RoleData(
                        RoleNames.PROCESS_TEAM_LEADER,
                        "Leads the process team"
                ),
                new RoleData(
                        RoleNames.PROCESS_TEAM_MEMBER,
                        "Handles process operations"
                ),
                new RoleData(
                        RoleNames.CLIENT_ACCOUNT_MANAGER,
                        "Manages client accounts"
                ),
                new RoleData(
                        RoleNames.QC_COMPONENT_MEMBER,
                        "Performs component level quality checks"
                ),
                new RoleData(
                        RoleNames.QC_CASE_MEMBER,
                        "Performs case level quality checks"
                ),
                new RoleData(
                        RoleNames.CLIENT_ADMIN,
                        "Manages users and operations for a client"
                ),
                new RoleData(
                        RoleNames.HR_USER,
                        "Handles HR related activities"
                )
        );

        for (RoleData roleData : roles) {

            if (roleRepository.existsByRoleName(roleData.roleName())) {
                continue;
            }

            Role role = new Role();

            role.setRoleName(roleData.roleName());
            role.setDescription(roleData.description());
            role.setIsActive(true);

            roleRepository.save(role);
        }
    }

    private record RoleData(
            String roleName,
            String description
    ) {
    }
}