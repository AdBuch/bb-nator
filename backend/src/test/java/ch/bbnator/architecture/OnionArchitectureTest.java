package ch.bbnator.architecture;

import ch.bbnator.architecture.fixture.domain.IllegalDomainDependency;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Guards the dependency rule from docs/architecture/MODULE_TEMPLATE.md. */
class OnionArchitectureTest {
    private static final JavaClasses PRODUCTION_CLASSES = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("ch.bbnator");

    private static final ArchRule NO_FRAMEWORK_IN_CORE =
            noClasses().that().resideInAnyPackage("..domain..", "..application..")
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "org.springframework..", "jakarta.persistence..",
                            "jakarta.servlet..", "org.hibernate..");

    @Test
    void corePackagesAreNotEmptySoRulesCannotPassVacuously() {
        assertTrue(PRODUCTION_CLASSES.stream().anyMatch(
                javaClass -> javaClass.getPackageName().endsWith(".domain")),
                "No production domain classes found; check ArchUnit package patterns");
        assertTrue(PRODUCTION_CLASSES.stream().anyMatch(
                javaClass -> javaClass.getPackageName().endsWith(".application")),
                "No production application classes found; check ArchUnit package patterns");
    }

    @Test
    void fixtureWithForbiddenDependencyMustFailTheArchitectureRule() {
        // Mutation control: a test-scoped, intentionally illegal domain class must be rejected.
        JavaClasses invalid = new ClassFileImporter().importClasses(IllegalDomainDependency.class);
        assertThrows(AssertionError.class, () -> NO_FRAMEWORK_IN_CORE.check(invalid));
    }

    @Test
    void domainAndApplicationAreFrameworkFree() {
        NO_FRAMEWORK_IN_CORE.check(PRODUCTION_CLASSES);
    }

    @Test
    void domainDoesNotDependOnOuterLayers() {
        noClasses().that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "..application..", "..infrastructure..", "..interfaces..")
                .check(PRODUCTION_CLASSES);
    }

    @Test
    void applicationDoesNotDependOnAdapters() {
        noClasses().that().resideInAPackage("..application..")
                .should().dependOnClassesThat().resideInAnyPackage("..infrastructure..", "..interfaces..")
                .check(PRODUCTION_CLASSES);
    }
}
