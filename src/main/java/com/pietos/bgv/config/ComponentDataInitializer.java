package com.pietos.bgv.config;


import com.pietos.bgv.repository.ComponentRepository;
import org.springframework.boot.CommandLineRunner;


import java.util.List;

@org.springframework.stereotype.Component
public class ComponentDataInitializer implements CommandLineRunner {

    private final ComponentRepository componentRepository;

    public ComponentDataInitializer(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    @Override
    public void run(String... args) {

        List<String> componentNames = List.of(
                "Education",
                "Digital Address",
                "Physical Address",
                "India Court Record Database Check",
                "Police (Criminal)",
                "Employment",
                "Reference",
                "India Credit Default Database Check",
                "Identity",
                "Passport",
                "Drug",
                "Driving License",
                "Directorship",
                "Reputation",
                "Credit",
                "Global Database Check",
                "Politically Exposed Person",
                "Adverse Media",
                "Right to Work",
                "Address Validation",
                "Company Credit Report",
                "CV Reconciliation",
                "Document - Certificate Verification",
                "Finger Printing Check",
                "Gap Verification",
                "5 Panel Drug Test",
                "7 Panel Drug Test",
                "9 Panel Drug Test",
                "10 Panel Drug Test",
                "11 Panel Drug Test",
                "UAN"
        );

        for (String componentName : componentNames) {

            String componentCode = generateComponentCode(componentName);

            if (componentRepository.existsByComponentName(componentName)) {
                continue;
            }

            if (componentRepository.existsByComponentCode(componentCode)) {
                continue;
            }

            com.pietos.bgv.entity.Component component =
                    new com.pietos.bgv.entity.Component();

            component.setComponentName(componentName);
            component.setComponentCode(componentCode);
            component.setIsActive(true);

            componentRepository.save(component);
        }
        }
    

    private String generateComponentCode(String componentName) {

    	return componentName
                .trim()
                .toUpperCase()
                .replaceAll("[^A-Z0-9]+", "_")
                .replaceAll("^_|_$", "");
    }
}